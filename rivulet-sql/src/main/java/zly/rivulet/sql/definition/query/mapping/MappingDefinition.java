package zly.rivulet.sql.definition.query.mapping;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.SelectMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.preparser.SqlPreParser;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

import java.lang.reflect.Method;

public class MappingDefinition {

    private SingleValueElementDefinition definition;

    /**
     * 引用别名，就是当前字段所属范围的引用表别名
     **/
    private SQLAliasManager.AliasFlag referenceAlias;

    private SQLAliasManager.AliasFlag aliasFlag;

    public MappingDefinition(FieldDefinition fieldDefinition) {
        this.definition = fieldDefinition;
    }
    public MappingDefinition(SqlPreParseHelper sqlPreParseHelper, SingleValueElementDesc<?, ?> desc, SelectMapping<?, ?> itemMapper) {
        this.definition = sqlPreParseHelper.parse(desc);
        SQLAliasManager aliasManager = sqlPreParseHelper.getAliasManager();
        this.aliasFlag = aliasManager.createFlag();

        QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();
    }

    public SingleValueElementDefinition parse(SqlPreParseHelper sqlPreParseHelper, SingleValueElementDesc<?, ?> singleValueElementDesc) {
        QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();
        SqlPreParser sqlPreParser = sqlPreParseHelper.getSqlPreParser();
        Method method = sqlPreParseHelper.getMethod();
        if (singleValueElementDesc instanceof FieldMapping) {
            FieldMapping<?, ?> fieldMapping = (FieldMapping<?, ?>) singleValueElementDesc;
            return currNode.parseField(fieldMapping);
        } else if (singleValueElementDesc instanceof SqlQueryMetaDesc) {
            SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) sqlPreParser.parse((SqlQueryMetaDesc<?, ?>) singleValueElementDesc, method);
            QueryProxyNode subQueryNode = sqlPreParseHelper.getCurrNode();
            currNode.addSelectNode(subQueryNode);
            sqlPreParseHelper.setCurrNode(currNode);
        } else if (singleValueElementDesc instanceof Param) {
            return sqlParamDefinitionManager.register((Param<?>) singleValueElementDesc);
//        } else if (singleValueElementDesc instanceof Function) {
        }
        return null;
    }

}
