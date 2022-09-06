package zly.rivulet.sql.parser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.DescDefineException;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.parser.Parser;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definition.query.HalfBlueprint;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.update.SqlUpdateDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.update.SqlUpdateMetaDesc;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.HashMap;
import java.util.Map;

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

    private final Map<String, Blueprint> key_queryDefinition_map = new HashMap<>();

    public SqlParser(WarehouseManager warehouseManager, SqlDefiner definer, SqlRivuletProperties configProperties, ConvertorManager convertorManager) {
        this.warehouseManager = warehouseManager;
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
        this.definer = definer;
    }


    @Override
    public Blueprint parseByKey(String key) {
        WholeDesc wholeDesc = warehouseManager.getWholeDesc(key);
        if (wholeDesc == null) {
            throw DescDefineException.noMatchDescKey();
        }

        SqlParserPortableToolbox sqlPreParseHelper = new SqlParserPortableToolbox(this);
        return this.parseByDesc(wholeDesc, sqlPreParseHelper);
    }

    public Blueprint parseByKey(String key, SqlParserPortableToolbox sqlPreParseHelper) {
        WholeDesc wholeDesc = warehouseManager.getWholeDesc(key);
        if (wholeDesc == null) {
            throw DescDefineException.noMatchDescKey();
        }
        return this.parseByDesc(wholeDesc, sqlPreParseHelper);
    }

    @Override
    public Blueprint parseByDesc(WholeDesc wholeDesc) {
        SqlParserPortableToolbox sqlPreParseHelper = new SqlParserPortableToolbox(this);
        return this.parseByDesc(wholeDesc, sqlPreParseHelper);
    }

    public Blueprint parseByDesc(WholeDesc wholeDesc, SqlParserPortableToolbox sqlPreParseHelper) {
        Blueprint blueprint = key_queryDefinition_map.get(wholeDesc.getKey());
        if (HalfBlueprint.isHalf(blueprint)) {
            // 循环嵌套子查询
            throw SQLDescDefineException.subQueryLoopNesting();
        }
        // 开始解析前先塞一个标识，用于解决循环嵌套子查询
        key_queryDefinition_map.put(wholeDesc.getKey(), HalfBlueprint.instance);

        if (wholeDesc instanceof SqlQueryMetaDesc) {
            // 查询方法
            blueprint = new SqlQueryDefinition(sqlPreParseHelper, wholeDesc);
//        } else if () {
//            // 新增
        } else if (wholeDesc instanceof SqlUpdateMetaDesc) {
            // 修改
            blueprint = new SqlUpdateDefinition(sqlPreParseHelper, wholeDesc);

//        } else if () {
//            // 删除

        } else {
            throw UnbelievableException.unknownType();
        }

        key_queryDefinition_map.put(wholeDesc.getKey(), blueprint);
        return blueprint;
    }


    @Override
    public Blueprint parseInsertByMeta(ModelMeta modelMeta) {
        return null;
    }

    @Override
    public Blueprint parseUpdateByMeta(ModelMeta modelMeta) {
        return null;
    }

    @Override
    public Blueprint parseDeleteByMeta(ModelMeta modelMeta) {
        return null;
    }

    @Override
    public Blueprint parseSelectByMeta(ModelMeta modelMeta) {
        return null;
    }

    @Override
    public Definer getDefiner() {
        return this.definer;
    }

    public SqlRivuletProperties getConfigProperties() {
        return configProperties;
    }

    public ConvertorManager getConvertorManager() {
        return convertorManager;
    }
}
