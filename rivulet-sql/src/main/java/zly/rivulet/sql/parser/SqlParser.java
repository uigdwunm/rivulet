package zly.rivulet.sql.parser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.custom.CustomDesc;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.parser.Analyzer;
import zly.rivulet.base.parser.Parser;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;
import zly.rivulet.base.utils.View;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.SQLCustomDefinition;
import zly.rivulet.sql.definition.delete.SqlDeleteDefinition;
import zly.rivulet.sql.definition.insert.SQLInsertDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.update.SqlUpdateDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.update.SqlUpdateMetaDesc;
import zly.rivulet.sql.parser.proxy_node.ProxyNodeManager;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Description 解析器
 * 不能并发(key_queryDefinition_map)
 *
 * @author zhaolaiyuan
 * Date 2022/9/6 8:39
 **/
public class SqlParser implements Parser {

    private final SqlRivuletProperties configProperties;

    private final ConvertorManager convertorManager;

    private final SqlDefiner definer;

    private final WarehouseManager warehouseManager;

    private final ProxyNodeManager proxyNodeManager = new ProxyNodeManager();

    private final List<Analyzer> analyzerList = new CopyOnWriteArrayList<>();

    private final TwofoldConcurrentHashMap<ModelMeta, RivuletFlag, Blueprint> modelMetaFlagBlueprintMap = new TwofoldConcurrentHashMap<>();

    public SqlParser(WarehouseManager warehouseManager, SqlDefiner definer, SqlRivuletProperties configProperties) {
        this.configProperties = configProperties;
        this.convertorManager = definer.getConvertorManager();
        this.definer = definer;
        this.warehouseManager = warehouseManager;
    }

    @Override
    public Blueprint parse(WholeDesc wholeDesc) {
        SqlParserPortableToolbox sqlPreParseHelper = new SqlParserPortableToolbox(this);
        Blueprint blueprint = this.parse(wholeDesc, sqlPreParseHelper);
        blueprint = this.analyze(blueprint);
        return blueprint;
    }

    private Blueprint analyze(Blueprint blueprint) {
        for (Analyzer analyzer : analyzerList) {
            blueprint = analyzer.analyze(blueprint);
        }
        return blueprint;
    }

    /**
     * Description 外部直接调用这个方法，一般是用于嵌套解析中的
     *
     * @author zhaolaiyuan
     * Date 2022/12/31 9:41
     **/
    public Blueprint parse(WholeDesc wholeDesc, SqlParserPortableToolbox toolbox) {
        // 检查有没有循环嵌套的子查询，并保存当前desc，后续解析继续检查
        toolbox.checkSubQueryCycle(wholeDesc);

        Blueprint blueprint;
        if (wholeDesc instanceof SqlQueryMetaDesc) {
            // 查询方法
            blueprint = new SqlQueryDefinition(toolbox, wholeDesc);
//        } else if () {
//            // 新增不支持自定义desc操作
        } else if (wholeDesc instanceof SqlUpdateMetaDesc) {
            // 修改
            blueprint = new SqlUpdateDefinition(toolbox, wholeDesc);

//        } else if () {
//            // 删除不支持自定义desc操作

        } else {
            throw UnbelievableException.unknownType();
        }

        // 解析完成，撤销检查
        toolbox.finishParse(wholeDesc);

        return blueprint;
    }

    @Override
    public void addAnalyzer(Analyzer analyzer) {
        analyzerList.add(analyzer);
    }

    @Override
    public Blueprint parseInsertByMeta(ModelMeta modelMeta) {
        RivuletFlag flag = RivuletFlag.INSERT;
        Blueprint blueprint = modelMetaFlagBlueprintMap.get(modelMeta, flag);
        if (blueprint == null) {
            SQLModelMeta sqlModelMeta = (SQLModelMeta) modelMeta;
            View<SQLFieldMeta> primaryFieldMeta = sqlModelMeta.getPrimaryFieldMeta();
            if (primaryFieldMeta.size() != 1) {
                throw ParseException.noAvailablePrimaryKey(modelMeta);
            }
            SqlParserPortableToolbox toolbox = new SqlParserPortableToolbox(this);
            blueprint = new SQLInsertDefinition(sqlModelMeta, toolbox);
            blueprint = this.analyze(blueprint);
            modelMetaFlagBlueprintMap.put(modelMeta, flag, blueprint);
        }
        return blueprint;
    }

    @Override
    public Blueprint parseUpdateByMeta(ModelMeta modelMeta) {
        RivuletFlag flag = RivuletFlag.UPDATE;
        Blueprint blueprint = modelMetaFlagBlueprintMap.get(modelMeta, flag);
        if (blueprint == null) {
            View<SQLFieldMeta> primaryFieldMeta = ((SQLModelMeta) modelMeta).getPrimaryFieldMeta();
            if (primaryFieldMeta.size() != 1) {
                throw ParseException.noAvailablePrimaryKey(modelMeta);
            }
            SqlParserPortableToolbox toolbox = new SqlParserPortableToolbox(this);
            blueprint = new SqlUpdateDefinition(toolbox, (SQLModelMeta) modelMeta, primaryFieldMeta.get(0));
            blueprint = this.analyze(blueprint);
            modelMetaFlagBlueprintMap.put(modelMeta, flag, blueprint);
        }
        return blueprint;
    }

    @Override
    public Blueprint parseDeleteByMeta(ModelMeta modelMeta) {
        RivuletFlag flag = RivuletFlag.DELETE;
        Blueprint blueprint = modelMetaFlagBlueprintMap.get(modelMeta, flag);
        if (blueprint == null) {
            View<SQLFieldMeta> primaryFieldMeta = ((SQLModelMeta) modelMeta).getPrimaryFieldMeta();
            if (primaryFieldMeta.size() != 1) {
                throw ParseException.noAvailablePrimaryKey(modelMeta);
            }
            SqlParserPortableToolbox toolbox = new SqlParserPortableToolbox(this);
            blueprint = new SqlDeleteDefinition(toolbox, (SQLModelMeta) modelMeta, primaryFieldMeta.get(0));
            blueprint = this.analyze(blueprint);
            modelMetaFlagBlueprintMap.put(modelMeta, flag, blueprint);
        }
        return blueprint;
    }

    @Override
    public Blueprint parseSelectByMeta(ModelMeta modelMeta) {
        RivuletFlag flag = RivuletFlag.QUERY;
        Blueprint blueprint = modelMetaFlagBlueprintMap.get(modelMeta, flag);
        if (blueprint == null) {
            View<SQLFieldMeta> primaryFieldMeta = ((SQLModelMeta) modelMeta).getPrimaryFieldMeta();
            if (primaryFieldMeta.size() != 1) {
                throw ParseException.noAvailablePrimaryKey(modelMeta);
            }
            SqlParserPortableToolbox toolbox = new SqlParserPortableToolbox(this);
            blueprint = new SqlQueryDefinition(toolbox, (SQLModelMeta) modelMeta, primaryFieldMeta.get(0));
            blueprint = this.analyze(blueprint);
            modelMetaFlagBlueprintMap.put(modelMeta, flag, blueprint);
        }
        return blueprint;
    }

    @Override
    public SqlDefiner getDefiner() {
        return this.definer;
    }

    public SQLCustomDefinition parseCustom(String key, CustomDesc customDesc) {
        QueryProxyNode queryProxyNode = proxyNodeManager.getQueryProxyNode(key);
        List<SingleValueElementDefinition> singleValueList = customDesc.getSingleValueList().stream()
            .map(singleValueElementDesc -> SqlParserPortableToolbox.parseSingleValueForCustom(queryProxyNode, singleValueElementDesc))
            .collect(Collectors.toList());
        return new SQLCustomDefinition(singleValueList, customDesc.getCustomCollect());
    }

    public SqlRivuletProperties getConfigProperties() {
        return configProperties;
    }

    public ConvertorManager getConvertorManager() {
        return convertorManager;
    }

    public ProxyNodeManager getProxyModelManager() {
        return proxyNodeManager;
    }

    public WarehouseManager getWarehouseManager() {
        return warehouseManager;
    }
}
