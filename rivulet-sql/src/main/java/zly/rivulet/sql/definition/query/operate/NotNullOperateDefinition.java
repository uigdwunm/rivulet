package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class NotNullOperateDefinition extends OperateDefinition {

    private SingleValueElementDefinition elementDesc;

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
    public NotNullOperateDefinition forAnalyze() {
        return null;
    }
}
