package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;
import zly.rivulet.sql.parser.node.QueryProxyNode;

public abstract class OperateDefinition extends AbstractDefinition {
    protected OperateDefinition(CheckCondition checkCondition, ParamReceiptManager paramReceiptManager) {
        super(checkCondition, paramReceiptManager);
    }

    @Override
    public abstract OperateDefinition forAnalyze();

    protected SingleValueElementDefinition parse(SqlParserPortableToolbox toolbox, SingleValueElementDesc<?, ?> singleValueElementDesc) {
        QueryProxyNode currNode = toolbox.getCurrNode();
        if (singleValueElementDesc instanceof FieldMapping) {
            FieldMapping<Object, Object> fieldMapping = (FieldMapping<Object, Object>) singleValueElementDesc;
            return currNode.parseField(fieldMapping);
        } else if (singleValueElementDesc instanceof JoinFieldMapping) {
            JoinFieldMapping<Object> joinFieldMapping = (JoinFieldMapping<Object>) singleValueElementDesc;
            return currNode.parseField(joinFieldMapping);
        } else if (singleValueElementDesc instanceof SqlQueryMetaDesc) {
            SqlParser sqlPreParser = toolbox.getSqlPreParser();
            SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) sqlPreParser.parseByDesc((SqlQueryMetaDesc<?, ?>) singleValueElementDesc, toolbox);
            QueryProxyNode subQueryNode = toolbox.getCurrNode();
            currNode.addConditionSubQueryNode(subQueryNode, sqlQueryDefinition);
            // 这里替换回来
            toolbox.setCurrNode(currNode);
            return sqlQueryDefinition;
        } else if (singleValueElementDesc instanceof Param) {
            ParamReceiptManager paramReceiptManager = toolbox.getParamDefinitionManager();
            return paramReceiptManager.registerParam((Param<?>) singleValueElementDesc);
//        } else if (singleValueElementDesc instanceof Function) {
        }
        throw UnbelievableException.unknownType();
    }
}
