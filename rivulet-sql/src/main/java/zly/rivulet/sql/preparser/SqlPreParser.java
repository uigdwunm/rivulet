package zly.rivulet.sql.preparser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.annotations.RivuletQueryMapper;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.DescDefineException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.preparser.PreParser;
import zly.rivulet.base.preparser.helper.PreParseHelper;
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

    private final Map<String, WholeDesc> wholeDescMap;

    private final Map<String, FinalDefinition> key_queryDefinition_map = new HashMap<>();

    public SqlPreParser(Map<String, WholeDesc> wholeDescMap, SqlDefiner definer, SqlRivuletProperties configProperties, ConvertorManager convertorManager) {
        this.wholeDescMap = wholeDescMap;
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
        this.definer = definer;
    }


    @Override
    public FinalDefinition parse(String key, Method method) {
        WholeDesc wholeDesc = wholeDescMap.get(key);
        if (wholeDesc == null) {
            throw DescDefineException.noMatchDescKey();
        }

        return this.parse(wholeDesc, method);
    }

    public FinalDefinition parse(String key, SqlPreParseHelper sqlPreParseHelper) {
        WholeDesc wholeDesc = wholeDescMap.get(key);
        if (wholeDesc == null) {
            throw DescDefineException.noMatchDescKey();
        }
        return this.parse(wholeDesc, sqlPreParseHelper);
    }

    public FinalDefinition parse(WholeDesc wholeDesc, Method method) {
        SqlPreParseHelper sqlPreParseHelper = new SqlPreParseHelper(this, method);
        return this.parse(wholeDesc, sqlPreParseHelper);
    }

    private FinalDefinition parse(WholeDesc wholeDesc, SqlPreParseHelper sqlPreParseHelper) {
        Method method = sqlPreParseHelper.getMethod();

        if (wholeDesc instanceof SqlQueryMetaDesc) {
            RivuletQueryMapper annotation = method.getAnnotation(RivuletQueryMapper.class);
            // 开始解析前先塞一个标识，用于解决循环嵌套子查询
            key_queryDefinition_map.put(annotation.value(), HalfFinalDefinition.instance);
            // 查询方法
            SqlQueryDefinition sqlQueryDefinition = new SqlQueryDefinition(sqlPreParseHelper, wholeDesc);
            key_queryDefinition_map.put(annotation.value(), sqlQueryDefinition);
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
