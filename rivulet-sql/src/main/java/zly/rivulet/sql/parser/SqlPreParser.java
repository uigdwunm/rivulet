package zly.rivulet.sql.parser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.DescDefineException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.parser.PreParser;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definition.query.HalfBlueprint;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.HashMap;
import java.util.Map;

public class SqlPreParser implements PreParser {

    private final SqlRivuletProperties configProperties;

    private final ConvertorManager convertorManager;

    private final SqlDefiner definer;

    private final WarehouseManager warehouseManager;

    private final Map<String, Blueprint> key_queryDefinition_map = new HashMap<>();

    public SqlPreParser(WarehouseManager warehouseManager, SqlDefiner definer, SqlRivuletProperties configProperties, ConvertorManager convertorManager) {
        this.warehouseManager = warehouseManager;
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
        this.definer = definer;
    }


    @Override
    public Blueprint parse(String key) {
        WholeDesc wholeDesc = warehouseManager.getWholeDesc(key);
        if (wholeDesc == null) {
            throw DescDefineException.noMatchDescKey();
        }

        SqlParserPortableToolbox sqlPreParseHelper = new SqlParserPortableToolbox(this);
        return this.parse(wholeDesc, sqlPreParseHelper);
    }

    @Override
    public Blueprint parse(WholeDesc wholeDesc) {
        SqlParserPortableToolbox sqlPreParseHelper = new SqlParserPortableToolbox(this);
        return this.parse(wholeDesc, sqlPreParseHelper);
    }

    public Blueprint parse(String key, SqlParserPortableToolbox sqlPreParseHelper) {
        WholeDesc wholeDesc = warehouseManager.getWholeDesc(key);
        if (wholeDesc == null) {
            throw DescDefineException.noMatchDescKey();
        }
        return this.parse(wholeDesc, sqlPreParseHelper);
    }

    public Blueprint parse(WholeDesc wholeDesc, SqlParserPortableToolbox sqlPreParseHelper) {

        if (wholeDesc instanceof SqlQueryMetaDesc) {
            // 开始解析前先塞一个标识，用于解决循环嵌套子查询
            key_queryDefinition_map.put(wholeDesc.getKey(), HalfBlueprint.instance);
            // 查询方法
            SqlQueryDefinition sqlQueryDefinition = new SqlQueryDefinition(sqlPreParseHelper, wholeDesc);
            key_queryDefinition_map.put(wholeDesc.getKey(), sqlQueryDefinition);
            return sqlQueryDefinition;
//        } else if () {
//            // 新增
//        } else if () {
//            // 修改
//        } else if () {
//            // 删除

        } else {
            throw UnbelievableException.unknownType();
        }
    }

    public SqlRivuletProperties getConfigProperties() {
        return configProperties;
    }

    public ConvertorManager getConvertorManager() {
        return convertorManager;
    }

    public SqlDefiner getSqlDefiner() {
        return definer;
    }
}
