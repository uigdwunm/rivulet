package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.sql.describer.query.condition.Condition;
import zly.rivulet.sql.describer.query.condition.ConditionElement;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

public class BetweenOperateDefinition extends OperateDefinition {

    public BetweenOperateDefinition(SqlPreParseHelper sqlPreParseHelper, Condition<?, ?> condition) {
        this(sqlPreParseHelper, (ConditionElement<?, ?>) condition);
    }

    public BetweenOperateDefinition(SqlPreParseHelper sqlPreParseHelper, ConditionElement<?, ?> condition) {
        super(condition.getCheckCondition(), sqlPreParseHelper.getSqlParamDefinitionManager());
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
