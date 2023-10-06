package zly.rivulet.sql.definition.query_.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.definition.query_.mapping.MapDefinition;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.parser.toolbox_.SQLParserPortableToolbox;

public class GTEOperateDefinition extends OperateDefinition {

    private final SingleValueElementDefinition leftElement;

    private final SingleValueElementDefinition rightElement;

    private GTEOperateDefinition(CheckCondition checkCondition, SingleValueElementDefinition leftElement, SingleValueElementDefinition rightElement) {
        super(checkCondition, null);
        this.leftElement = leftElement;
        this.rightElement = rightElement;
    }

    public GTEOperateDefinition(SQLParserPortableToolbox sqlPreParseHelper, Condition<?, ?> condition) {
        this(sqlPreParseHelper, (ConditionElement<?, ?>) condition);
    }

    public GTEOperateDefinition(SQLParserPortableToolbox toolbox, ConditionElement<?, ?> condition) {
        super(condition.getCheckCondition(), toolbox.getParamReceiptManager());
        SingleValueElementDesc<?, ?> leftFieldMapped = condition.getLeftFieldMapped();
        SingleValueElementDesc<?, ?> rightFieldMapped = condition.getRightFieldMappeds()[0];

        this.leftElement = toolbox.parseSingleValueForCondition(leftFieldMapped);
        this.rightElement = toolbox.parseSingleValueForCondition(rightFieldMapped);
    }

    public GTEOperateDefinition(SQLParserPortableToolbox toolbox, MapDefinition mapDefinition, Param<?> param, CheckCondition checkCondition) {
        super(checkCondition, toolbox.getParamReceiptManager());

        this.leftElement =  mapDefinition;
        this.rightElement = toolbox.parseSingleValueForCondition(param);
    }

    public SingleValueElementDefinition getLeftElement() {
        return leftElement;
    }

    public SingleValueElementDefinition getRightElement() {
        return rightElement;
    }

    @Override
    public Copier copier() {
        return new Copier(leftElement, rightElement);
    }

    public class Copier implements Definition.Copier {

        private SingleValueElementDefinition leftElement;

        private SingleValueElementDefinition rightElement;

        public Copier(SingleValueElementDefinition leftElement, SingleValueElementDefinition rightElement) {
            this.leftElement = leftElement;
            this.rightElement = rightElement;
        }

        public void setLeftElement(SingleValueElementDefinition leftElement) {
            this.leftElement = leftElement;
        }

        public void setRightElement(SingleValueElementDefinition rightElement) {
            this.rightElement = rightElement;
        }

        @Override
        public GTEOperateDefinition copy() {
            return new GTEOperateDefinition(checkCondition, leftElement, rightElement);
        }
    }
}
