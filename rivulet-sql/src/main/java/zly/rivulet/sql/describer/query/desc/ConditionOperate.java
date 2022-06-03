package zly.rivulet.sql.describer.query.desc;

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
    NOT_NULL(NotNullOperateDefinition::new)
    
    ;

    private final BiFunction<SqlPreParseHelper, AbstractCondition<?, ?, ?>, OperateDefinition> operate;


    ConditionOperate(BiFunction<SqlPreParseHelper, AbstractCondition<?, ?, ?>, OperateDefinition> operate) {
        this.operate = operate;
    }

    public OperateDefinition createDefinition(SqlPreParseHelper sqlPreParseHelper, AbstractCondition<?, ?, ?> condition) {
        return operate.apply(sqlPreParseHelper, condition);
    }

}
