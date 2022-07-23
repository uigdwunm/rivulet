package zly.rivulet.sql.preparser.helper;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.preparser.helper.PreParseHelper;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.preparser.SqlParamDefinitionManager;
import zly.rivulet.sql.preparser.SqlPreParser;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

public class SqlPreParseHelper implements PreParseHelper {

    private final SqlPreParser sqlPreParser;

    private final ParamDefinitionManager paramDefinitionManager;

    private QueryProxyNode currNode;

    private final SqlRivuletProperties configProperties;

    public SqlPreParseHelper(SqlPreParser sqlPreParser, ParamDefinitionManager paramDefinitionManager) {
        this.sqlPreParser = sqlPreParser;
        this.paramDefinitionManager = paramDefinitionManager;
        this.configProperties = sqlPreParser.getConfigProperties();
    }

    public SqlPreParser getSqlPreParser() {
        return sqlPreParser;
    }

    public ParamDefinitionManager getParamDefinitionManager() {
        return paramDefinitionManager;
    }

    public QueryProxyNode getCurrNode() {
        return currNode;
    }

    public void setCurrNode(QueryProxyNode currNode) {
        this.currNode = currNode;
    }

    public SqlRivuletProperties getConfigProperties() {
        return configProperties;
    }

    public SingleValueElementDefinition parse(SingleValueElementDesc<?, ?> singleValueElementDesc) {
        if (singleValueElementDesc instanceof FieldMapping) {
            FieldMapping<?, ?> fieldMapping = (FieldMapping<?, ?>) singleValueElementDesc;
            return currNode.parseField((FieldMapping<Object, Object>) fieldMapping);
        } else if (singleValueElementDesc instanceof SqlQueryMetaDesc) {
            return (SqlQueryDefinition) sqlPreParser.parse((SqlQueryMetaDesc<?, ?>) singleValueElementDesc, this);
        } else if (singleValueElementDesc instanceof Param) {
            return paramDefinitionManager.register((Param<?>) singleValueElementDesc);
//        } else if (singleValueElementDesc instanceof Function) {
        }
        return null;
    }
}
