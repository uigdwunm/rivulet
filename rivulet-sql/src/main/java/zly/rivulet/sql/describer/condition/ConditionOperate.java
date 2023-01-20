package zly.rivulet.sql.describer.condition;

import zly.rivulet.sql.definition.query.operate.*;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.function.BiFunction;

public enum ConditionOperate {
    // =
    EQ(EqOperateDefinition::new),

    // !=
    NE(NotEqOperateDefinition::new),

    // >
    GT(GTOperateDefinition::new),

    // >=
    GTE(GTEOperateDefinition::new),

    // <
    LT(LTOperateDefinition::new),

    // <=
    LTE(LTEOperateDefinition::new),

    BETWEEN(BetweenOperateDefinition::new),
    IN(InOperateDefinition::new),
//    NOT_IN("not in"),
//    IS_NULL(""),
    NOT_NULL(NotNullOperateDefinition::new),
    AND(AndOperateDefinition::new),
    OR(OrOperateDefinition::new),

    ;

    private final BiFunction<SqlParserPortableToolbox, Condition<?, ?>, OperateDefinition> operate;


    ConditionOperate(BiFunction<SqlParserPortableToolbox, Condition<?, ?>, OperateDefinition> operate) {
        this.operate = operate;
    }

    public OperateDefinition createDefinition(SqlParserPortableToolbox sqlPreParseHelper, Condition<?, ?> condition) {
        return operate.apply(sqlPreParseHelper, condition);
    }

}
