package zly.rivulet.sql.parser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.custom.CustomDesc;
import zly.rivulet.base.exception.DescDefineException;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.parser.Parser;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;
import zly.rivulet.base.utils.View;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.SQLCustomDefinition;
import zly.rivulet.sql.definition.insert.SQLInsertDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.update.SqlUpdateDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.update.SqlUpdateMetaDesc;
import zly.rivulet.sql.parser.proxy_node.ProxyNodeManager;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.List;
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

    private final TwofoldConcurrentHashMap<ModelMeta, RivuletFlag, Blueprint> modelMetaFlagBlueprintMap = new TwofoldConcurrentHashMap<>();

    public SqlParser(WarehouseManager warehouseManager, SqlDefiner definer, SqlRivuletProperties configProperties, ConvertorManager convertorManager) {
        this.warehouseManager = warehouseManager;
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
        this.definer = definer;
    }

    public WholeDesc getWholeDesc(String key) {
        return warehouseManager.getWholeDesc(key);
    }

    @Override
    public Blueprint parseByKey(String key) {
        WholeDesc wholeDesc = this.getWholeDesc(key);
        if (wholeDesc == null) {
            throw DescDefineException.noMatchDescKey();
        }

        SqlParserPortableToolbox sqlPreParseHelper = new SqlParserPortableToolbox(this);
        return this.parseByDesc(wholeDesc, sqlPreParseHelper);
    }

    @Override
    public Blueprint parseByDesc(WholeDesc wholeDesc) {
        SqlParserPortableToolbox sqlPreParseHelper = new SqlParserPortableToolbox(this);
        return this.parseByDesc(wholeDesc, sqlPreParseHelper);
    }

    public Blueprint parseByDesc(WholeDesc wholeDesc, SqlParserPortableToolbox toolbox) {
        // 检查有没有循环嵌套的子查询，并保存当前desc，后续解析继续检查
        toolbox.checkSubQueryCycle(wholeDesc);

        Blueprint blueprint;
        if (wholeDesc instanceof SqlQueryMetaDesc) {
            // 查询方法
            blueprint = new SqlQueryDefinition(toolbox, wholeDesc);
//        } else if () {
//            // 新增
        } else if (wholeDesc instanceof SqlUpdateMetaDesc) {
            // 修改
            blueprint = new SqlUpdateDefinition(toolbox, wholeDesc);

//        } else if () {
//            // 删除

        } else {
            throw UnbelievableException.unknownType();
        }

        // 解析完成，撤销检查
        toolbox.finishParse(wholeDesc);

        return blueprint;
    }


    @Override
    public Blueprint parseInsertByMeta(ModelMeta modelMeta) {
        RivuletFlag flag = RivuletFlag.INSERT;
        Blueprint blueprint = modelMetaFlagBlueprintMap.get(modelMeta, flag);
        if (blueprint == null) {
            SQLModelMeta sqlModelMeta = (SQLModelMeta) modelMeta;
            View<SQLFieldMeta> primaryFieldMeta = sqlModelMeta.getPrimaryFieldMeta();
            if (primaryFieldMeta.size() != 1) {
                throw ParseException.noAvailablePrimaryKey();
            }
            SqlParserPortableToolbox toolbox = new SqlParserPortableToolbox(this);
            blueprint = new SQLInsertDefinition(sqlModelMeta, toolbox);
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
                throw ParseException.noAvailablePrimaryKey();
            }
            SqlParserPortableToolbox toolbox = new SqlParserPortableToolbox(this);
            blueprint = new SqlUpdateDefinition(toolbox, (SQLModelMeta) modelMeta, primaryFieldMeta.get(0));
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
                throw ParseException.noAvailablePrimaryKey();
            }
            SqlParserPortableToolbox sqlPreParseHelper = new SqlParserPortableToolbox(this);
            // TODO
//            blueprint = ;
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
                throw ParseException.noAvailablePrimaryKey();
            }
            SqlParserPortableToolbox toolbox = new SqlParserPortableToolbox(this);
            blueprint = new SqlQueryDefinition(toolbox, (SQLModelMeta) modelMeta, primaryFieldMeta.get(0));
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
}
