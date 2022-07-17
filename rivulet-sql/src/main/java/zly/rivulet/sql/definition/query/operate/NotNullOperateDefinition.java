package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.query.condition.Condition;
import zly.rivulet.sql.describer.query.condition.ConditionElement;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

public class NotNullOperateDefinition extends OperateDefinition {

    private SingleValueElementDefinition elementDesc;

    public NotNullOperateDefinition(SqlPreParseHelper sqlPreParseHelper, Condition<?, ?> condition) {
        this(sqlPreParseHelper, (ConditionElement<?, ?>) condition);
    }

    private NotNullOperateDefinition(SqlPreParseHelper sqlPreParseHelper, ConditionElement<?, ?> condition) {
        super(condition.getCheckCondition(), sqlPreParseHelper.getSqlParamDefinitionManager());
        SingleValueElementDesc<?, ?> elementDesc = condition.getLeftFieldMapped();

        this.elementDesc = sqlPreParseHelper.parse(elementDesc);
    }

    public SingleValueElementDefinition getElementDesc() {
        return elementDesc;
    }

    @Override
    public NotNullOperateDefinition forAnalyze() {
        return null;
    }
}
