package zly.rivulet.sql.definition.query.mapping;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.sql.describer.function.MFunctionDesc;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;
import zly.rivulet.sql.parser.node.FieldProxyNode;
import zly.rivulet.sql.parser.node.QueryProxyNode;

public class MapDefinition implements SQLSingleValueElementDefinition {

    private SetMapping<?, ?> selectField;

    private final SingleValueElementDefinition valueDefinition;

    /**
     * 引用别名，就是当前select字段的引用表别名
     **/
    private final SQLAliasManager.AliasFlag referenceAlias;

    /**
     * 别名，这个select字段自己的别名
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    public MapDefinition(FieldDefinition fieldDefinition, SQLAliasManager.AliasFlag referenceAlias, SQLAliasManager.AliasFlag aliasFlag) {
        this.valueDefinition = fieldDefinition;
        this.referenceAlias = referenceAlias;
        this.aliasFlag = aliasFlag;
    }

    public MapDefinition(SqlParserPortableToolbox sqlPreParseHelper, Mapping<?, ?, ?> item, FieldDefinition fieldDefinition) {
        this.valueDefinition = fieldDefinition;
        this.selectField = item.getMappingField();
        QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();
        this.referenceAlias = currNode.getAliasFlag();
        SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createFieldAlias(fieldDefinition.getFieldMeta().getOriginName());
        currNode.addSelectNode(new FieldProxyNode(currNode, aliasFlag));
        this.aliasFlag = aliasFlag;
    }

    public MapDefinition(SqlParserPortableToolbox sqlPreParseHelper, SqlQueryMetaDesc<?, ?> sqlQueryMetaDesc, SetMapping<?, ?> selectField) {
        this.selectField = selectField;
        QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();
        SqlParser sqlPreParser = sqlPreParseHelper.getSqlPreParser();
        SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) sqlPreParser.parseByDesc(sqlQueryMetaDesc, sqlPreParseHelper);
        QueryProxyNode subQueryNode = sqlPreParseHelper.getCurrNode();
        currNode.addSelectNode(subQueryNode, sqlQueryDefinition);
        // 这里替换回来
        sqlPreParseHelper.setCurrNode(currNode);
        this.valueDefinition = sqlQueryDefinition;
        // select中的子查询不属于任何node
        this.referenceAlias = currNode.getAliasFlag();
        this.aliasFlag = subQueryNode.getAliasFlag();
    }

    public MapDefinition(SqlParserPortableToolbox sqlPreParseHelper, Param<?> paramDesc, SetMapping<?, ?> selectField) {
        this.selectField = selectField;
        ParamReceiptManager paramReceiptManager = sqlPreParseHelper.getParamReceiptManager();
        this.valueDefinition = paramReceiptManager.registerParam(paramDesc);
        this.referenceAlias = sqlPreParseHelper.getCurrNode().getAliasFlag();
        this.aliasFlag = SQLAliasManager.createFieldAlias();
    }

    public MapDefinition(SqlParserPortableToolbox sqlPreParseHelper, MFunctionDesc<?, ?> functionDesc, SetMapping<?, ?> selectField) {
        // TODO
        this.selectField = selectField;
        this.valueDefinition = null;
        this.referenceAlias = sqlPreParseHelper.getCurrNode().getAliasFlag();
        this.aliasFlag = SQLAliasManager.createFieldAlias();
    }

    public SetMapping<?, ?> getSelectField() {
        return selectField;
    }

    public SingleValueElementDefinition getValueDefinition() {
        return valueDefinition;
    }

    public SQLAliasManager.AliasFlag getAliasFlag() {
        return aliasFlag;
    }

    public SQLAliasManager.AliasFlag getReferenceAlias() {
        return referenceAlias;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

}
