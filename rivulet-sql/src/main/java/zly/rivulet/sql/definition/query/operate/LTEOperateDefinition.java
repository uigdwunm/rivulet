package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class LTEOperateDefinition extends OperateDefinition {

    private final SingleValueElementDefinition leftElement;

    private final SingleValueElementDefinition rightElement;

    private LTEOperateDefinition(CheckCondition checkCondition, SingleValueElementDefinition leftElement, SingleValueElementDefinition rightElement) {
        super(checkCondition, null);
        this.leftElement = leftElement;
        this.rightElement = rightElement;
    }

    public LTEOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, Condition<?, ?> condition) {
        this(sqlPreParseHelper, (ConditionElement<?, ?>) condition);
    }

    public LTEOperateDefinition(SqlParserPortableToolbox toolbox, ConditionElement<?, ?> condition) {
        super(condition.getCheckCondition(), toolbox.getParamReceiptManager());
        SingleValueElementDesc<?, ?> leftFieldMapped = condition.getLeftFieldMapped();
        SingleValueElementDesc<?, ?> rightFieldMapped = condition.getRightFieldMappeds()[0];

        this.leftElement = toolbox.parseSingleValueForCondition(leftFieldMapped);
        this.rightElement = toolbox.parseSingleValueForCondition(rightFieldMapped);
    }

    public LTEOperateDefinition(SqlParserPortableToolbox toolbox, MapDefinition mapDefinition, Param<?> param, CheckCondition checkCondition) {
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
        public LTEOperateDefinition copy() {
            return new LTEOperateDefinition(checkCondition, leftElement, rightElement);
        }
    }
}
