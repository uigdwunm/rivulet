package zly.rivulet.sql.preparser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.DescDefineException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.preparser.PreParser;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definition.query.HalfFinalDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SqlPreParser implements PreParser {

    private final SqlRivuletProperties configProperties;

    private final ConvertorManager convertorManager;

    private final SqlDefiner definer;

    private final WarehouseManager warehouseManager;

    private final Map<String, FinalDefinition> key_queryDefinition_map = new HashMap<>();

    public SqlPreParser(WarehouseManager warehouseManager, SqlDefiner definer, SqlRivuletProperties configProperties, ConvertorManager convertorManager) {
        this.warehouseManager = warehouseManager;
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
        this.definer = definer;
    }


    @Override
    public FinalDefinition parse(String key, Method method) {
        WholeDesc wholeDesc = warehouseManager.getWholeDesc(key);
        if (wholeDesc == null) {
            throw DescDefineException.noMatchDescKey();
        }

        SqlParamDefinitionManager sqlParamDefinitionManager = new SqlParamDefinitionManager(method.getParameters(), this.convertorManager);
        SqlPreParseHelper sqlPreParseHelper = new SqlPreParseHelper(this, sqlParamDefinitionManager);
        return this.parse(wholeDesc, sqlPreParseHelper);
    }

    @Override
    public FinalDefinition parse(WholeDesc wholeDesc, ParamDefinitionManager paramDefinitionManager) {
        SqlPreParseHelper sqlPreParseHelper = new SqlPreParseHelper(this, paramDefinitionManager);
        return this.parse(wholeDesc, sqlPreParseHelper);
    }

    public FinalDefinition parse(String key, SqlPreParseHelper sqlPreParseHelper) {
        WholeDesc wholeDesc = warehouseManager.getWholeDesc(key);
        if (wholeDesc == null) {
            throw DescDefineException.noMatchDescKey();
        }
        return this.parse(wholeDesc, sqlPreParseHelper);
    }

    public FinalDefinition parse(WholeDesc wholeDesc, SqlPreParseHelper sqlPreParseHelper) {

        if (wholeDesc instanceof SqlQueryMetaDesc) {
            // 开始解析前先塞一个标识，用于解决循环嵌套子查询
            key_queryDefinition_map.put(wholeDesc.getKey(), HalfFinalDefinition.instance);
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
