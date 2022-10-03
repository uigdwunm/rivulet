package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class BetweenOperateDefinition extends OperateDefinition {

    public BetweenOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, Condition<?, ?> condition) {
        this(sqlPreParseHelper, (ConditionElement<?, ?>) condition);
    }

    public BetweenOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, ConditionElement<?, ?> condition) {
        super(condition.getCheckCondition(), sqlPreParseHelper.getParamReceiptManager());
    }

    @Override
    public BetweenOperateDefinition forAnalyze() {
        return null;
    }
}
