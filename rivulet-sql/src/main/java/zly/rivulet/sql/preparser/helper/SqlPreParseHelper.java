package zly.rivulet.sql.preparser.helper;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.preparser.helper.PreParseHelper;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.preparser.SQLProxyModelManager;
import zly.rivulet.sql.preparser.SqlParamDefinitionManager;
import zly.rivulet.sql.preparser.SqlPreParser;

import java.lang.reflect.Method;

public class SqlPreParseHelper implements PreParseHelper {

    private final SqlPreParser sqlPreParser;

    private final SqlParamDefinitionManager sqlParamDefinitionManager;

    private final WholeDesc wholeDesc;

    private final SQLProxyModelManager sqlProxyModelManager;

    private final SQLAliasManager aliasManager;

    private final SqlRivuletProperties configProperties;

    private final Method method;

    public SqlPreParseHelper(SqlPreParser sqlPreParser, WholeDesc wholeDesc, Method method) {
        this.sqlPreParser = sqlPreParser;
        this.sqlParamDefinitionManager = new SqlParamDefinitionManager(method.getParameters(), sqlPreParser.getConvertorManager());
        this.wholeDesc = wholeDesc;
        SqlRivuletProperties configProperties = sqlPreParser.getConfigProperties();
        SQLAliasManager aliasManager = new SQLAliasManager(configProperties.isUseShortAlias());
        this.aliasManager = aliasManager;
        this.sqlProxyModelManager = new SQLProxyModelManager(wholeDesc.getMainFrom(), sqlPreParser.getDefiner(), sqlPreParser, aliasManager, method);
        this.configProperties = configProperties;
        this.method = method;
    }

    public void startSub() {
        sqlProxyModelManager.startSub();
    }

    public void endSub() {
        sqlProxyModelManager.endSub();
    }

    public SqlPreParser getSqlPreParser() {
        return sqlPreParser;
    }

    public SqlParamDefinitionManager getSqlParamDefinitionManager() {
        return sqlParamDefinitionManager;
    }

    public WholeDesc getWholeDesc() {
        return wholeDesc;
    }

    public SQLProxyModelManager getSQLProxyModelManager() {
        return sqlProxyModelManager;
    }

    public SQLProxyModelManager getSqlProxyModelManager() {
        return sqlProxyModelManager;
    }

    public SQLAliasManager getAliasManager() {
        return aliasManager;
    }

    public SqlRivuletProperties getConfigProperties() {
        return configProperties;
    }

    public Method getMethod() {
        return method;
    }

    public SingleValueElementDefinition parse(SingleValueElementDesc<?, ?> singleValueElementDesc) {
        if (singleValueElementDesc instanceof FieldMapping) {
            FieldMapping<?, ?> fieldMapping = (FieldMapping<?, ?>) singleValueElementDesc;
            return sqlProxyModelManager.parseField(fieldMapping);
        } else if (singleValueElementDesc instanceof SqlQueryMetaDesc) {
            return (SqlQueryDefinition) sqlPreParser.parse((SqlQueryMetaDesc<?, ?>) singleValueElementDesc, method);
        } else if (singleValueElementDesc instanceof Param) {
            return sqlParamDefinitionManager.register((Param<?>) singleValueElementDesc);
//        } else if (singleValueElementDesc instanceof Function) {
        }
        return null;
    }
}
