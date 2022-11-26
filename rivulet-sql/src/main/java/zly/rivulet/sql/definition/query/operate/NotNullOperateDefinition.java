package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class NotNullOperateDefinition extends OperateDefinition {

    private final SingleValueElementDefinition elementDesc;

    private NotNullOperateDefinition(CheckCondition checkCondition, SingleValueElementDefinition elementDesc) {
        super(checkCondition, null);
        this.elementDesc = elementDesc;
    }

    public NotNullOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, Condition<?, ?> condition) {
        this(sqlPreParseHelper, (ConditionElement<?, ?>) condition);
    }

    private NotNullOperateDefinition(SqlParserPortableToolbox toolbox, ConditionElement<?, ?> condition) {
        super(condition.getCheckCondition(), toolbox.getParamReceiptManager());
        SingleValueElementDesc<?, ?> elementDesc = condition.getLeftFieldMapped();

        this.elementDesc = toolbox.parseSingleValueForCondition(elementDesc);
    }

    public SingleValueElementDefinition getElementDesc() {
        return elementDesc;
    }

    @Override
    public Copier copier() {
        return new Copier(elementDesc);
    }

    public class Copier implements Definition.Copier {
        private SingleValueElementDefinition elementDesc;

        private Copier(SingleValueElementDefinition elementDesc) {
            this.elementDesc = elementDesc;
        }

        public void setElementDesc(SingleValueElementDefinition elementDesc) {
            this.elementDesc = elementDesc;
        }

        @Override
        public NotNullOperateDefinition copy() {
            return new NotNullOperateDefinition(checkCondition, elementDesc);
        }
    }
}
