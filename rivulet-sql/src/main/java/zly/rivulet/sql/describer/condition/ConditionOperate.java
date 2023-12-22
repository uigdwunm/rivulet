package zly.rivulet.sql.describer.condition;

import zly.rivulet.sql.definition.query.operate.*;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.parser.toolbox.SQLParserPortableToolbox;

import java.util.function.BiFunction;

public enum ConditionOperate {
    // =
    EQ(EqOperateDefinition::new),

    // !=
    NE(NotEqOperateDefinition::new),

    // like
    LIKE(LikeOperateDefinition::new),

    // not like
    NOT_LIKE(NotLikeOperateDefinition::new),

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

    NOT_IN(NotInOperateDefinition::new),

    IS_NULL(IsNullOperateDefinition::new),
    NOT_NULL(NotNullOperateDefinition::new),
    AND(AndOperateDefinition::new),
    OR(OrOperateDefinition::new),

    ;

    private final BiFunction<SQLParserPortableToolbox, Condition, OperateDefinition> operate;


    ConditionOperate(BiFunction<SQLParserPortableToolbox, Condition, OperateDefinition> operate) {
        this.operate = operate;
    }

    public OperateDefinition createDefinition(SQLParserPortableToolbox sqlPreParseHelper, Condition condition) {
        return operate.apply(sqlPreParseHelper, condition);
    }

}
