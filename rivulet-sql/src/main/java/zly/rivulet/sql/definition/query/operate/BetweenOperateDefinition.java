package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class BetweenOperateDefinition extends OperateDefinition {

    private final SingleValueElementDefinition leftElement;

    private final SingleValueElementDefinition beforeElement;

    private final SingleValueElementDefinition afterElement;

    private BetweenOperateDefinition(
        CheckCondition checkCondition,
        SingleValueElementDefinition leftElement,
        SingleValueElementDefinition beforeElement,
        SingleValueElementDefinition afterElement
    ) {
        super(checkCondition, null);
        this.leftElement = leftElement;
        this.beforeElement = beforeElement;
        this.afterElement = afterElement;
    }

    public BetweenOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, Condition<?, ?> condition) {
        this(sqlPreParseHelper, (ConditionElement<?, ?>) condition);
    }

    public BetweenOperateDefinition(SqlParserPortableToolbox toolbox, ConditionElement<?, ?> condition) {
        super(condition.getCheckCondition(), toolbox.getParamReceiptManager());
        SingleValueElementDesc<?, ?> leftFieldMapped = condition.getLeftFieldMapped();
        SingleValueElementDesc<?, ?> beforeElement = condition.getRightFieldMappeds()[0];
        SingleValueElementDesc<?, ?> afterElement = condition.getRightFieldMappeds()[1];

        this.leftElement = toolbox.parseSingleValueForCondition(leftFieldMapped);
        this.beforeElement = toolbox.parseSingleValueForCondition(beforeElement);
        this.afterElement = toolbox.parseSingleValueForCondition(afterElement);
    }

    public SingleValueElementDefinition getLeftElement() {
        return leftElement;
    }

    public SingleValueElementDefinition getBeforeElement() {
        return beforeElement;
    }

    public SingleValueElementDefinition getAfterElement() {
        return afterElement;
    }

    @Override
    public Copier copier() {
        return new Copier(leftElement, beforeElement, afterElement);
    }

    public class Copier implements Definition.Copier {

        private SingleValueElementDefinition leftElement;

        private SingleValueElementDefinition beforeElement;

        private SingleValueElementDefinition afterElement;

        private Copier(SingleValueElementDefinition leftElement, SingleValueElementDefinition beforeElement, SingleValueElementDefinition afterElement) {
            this.leftElement = leftElement;
            this.beforeElement = beforeElement;
            this.afterElement = afterElement;
        }

        public void setLeftElement(SingleValueElementDefinition leftElement) {
            this.leftElement = leftElement;
        }

        public void setBeforeElement(SingleValueElementDefinition beforeElement) {
            this.beforeElement = beforeElement;
        }

        public void setAfterElement(SingleValueElementDefinition afterElement) {
            this.afterElement = afterElement;
        }

        @Override
        public BetweenOperateDefinition copy() {
            return new BetweenOperateDefinition(checkCondition, leftElement, beforeElement, afterElement);
        }
    }
}
