package zly.rivulet.sql.describer.query.condition;

import zly.rivulet.sql.definition.query.operate.BetweenOperateDefinition;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.definition.query.operate.NotNullOperateDefinition;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

import java.util.function.BiFunction;

public enum ConditionOperate {
    EQ(EqOperateDefinition::new),
//    GT(">"),
//    GTE(">="),
//    LT("<"),
//    LTE("<="),
//    NE("!="),
    BETWEEN(BetweenOperateDefinition::new),
//    IN("in"),
//    NOT_IN("not in"),
//    IS_NULL(""),
    NOT_NULL(NotNullOperateDefinition::new),
    AND(),
    OR(),

    ;

    private final BiFunction<SqlPreParseHelper, Condition<?, ?>, OperateDefinition> operate;


    ConditionOperate(BiFunction<SqlPreParseHelper, Condition<?, ?>, OperateDefinition> operate) {
        this.operate = operate;
    }

    public OperateDefinition createDefinition(SqlPreParseHelper sqlPreParseHelper, Condition<?, ?> condition) {
        return operate.apply(sqlPreParseHelper, condition);
    }

}
