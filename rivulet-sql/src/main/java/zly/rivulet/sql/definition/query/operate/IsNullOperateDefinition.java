package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class IsNullOperateDefinition extends OperateDefinition {

    private final SingleValueElementDefinition valueElementDefinition;

    private IsNullOperateDefinition(CheckCondition checkCondition, SingleValueElementDefinition valueElementDefinition) {
        super(checkCondition, null);
        this.valueElementDefinition = valueElementDefinition;
    }

    public IsNullOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, Condition<?, ?> condition) {
        this(sqlPreParseHelper, (ConditionElement<?, ?>) condition);
    }

    private IsNullOperateDefinition(SqlParserPortableToolbox toolbox, ConditionElement<?, ?> condition) {
        super(condition.getCheckCondition(), toolbox.getParamReceiptManager());
        SingleValueElementDesc<?, ?> elementDesc = condition.getLeftFieldMapped();

        this.valueElementDefinition = toolbox.parseSingleValueForCondition(elementDesc);
    }

    public SingleValueElementDefinition getValueElementDefinition() {
        return valueElementDefinition;
    }

    @Override
    public Copier copier() {
        return new Copier(valueElementDefinition);
    }

    public class Copier implements Definition.Copier {
        private SingleValueElementDefinition valueElementDefinition;

        private Copier(SingleValueElementDefinition valueElementDefinition) {
            this.valueElementDefinition = valueElementDefinition;
        }

        public void setValueElementDefinition(SingleValueElementDefinition valueElementDefinition) {
            this.valueElementDefinition = valueElementDefinition;
        }

        @Override
        public IsNullOperateDefinition copy() {
            return new IsNullOperateDefinition(checkCondition, valueElementDefinition);
        }
    }
}
