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

    private NotNullOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, ConditionElement<?, ?> condition) {
        super(condition.getCheckCondition(), sqlPreParseHelper.getParamDefinitionManager());
        SingleValueElementDesc<?, ?> elementDesc = condition.getLeftFieldMapped();

        this.elementDesc = super.parse(sqlPreParseHelper, elementDesc);
    }

    public SingleValueElementDefinition getElementDesc() {
        return elementDesc;
    }

    @Override
    public NotNullOperateDefinition forAnalyze() {
        return null;
    }
}
