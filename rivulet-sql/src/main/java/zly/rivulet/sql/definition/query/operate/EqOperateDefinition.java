package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.query.condition.Condition;
import zly.rivulet.sql.describer.query.condition.ConditionElement;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

public class EqOperateDefinition extends OperateDefinition {

    private SingleValueElementDefinition leftElement;

    private SingleValueElementDefinition rightElement;

    public EqOperateDefinition(SqlPreParseHelper sqlPreParseHelper, Condition<?, ?> condition) {
        this(sqlPreParseHelper, (ConditionElement<?, ?>) condition);
    }

    public EqOperateDefinition(SqlPreParseHelper sqlPreParseHelper, ConditionElement<?, ?> condition) {
        super(condition.getCheckCondition(), sqlPreParseHelper.getParamDefinitionManager());
        SingleValueElementDesc<?, ?> leftFieldMapped = condition.getLeftFieldMapped();
        SingleValueElementDesc<?, ?> rightFieldMapped = condition.getRightFieldMappeds()[0];

        this.leftElement = super.parse(sqlPreParseHelper, leftFieldMapped);
        this.rightElement = super.parse(sqlPreParseHelper, rightFieldMapped);
    }

    public SingleValueElementDefinition getLeftElement() {
        return leftElement;
    }

    public SingleValueElementDefinition getRightElement() {
        return rightElement;
    }

    @Override
    public EqOperateDefinition forAnalyze() {
        return null;
    }
}
