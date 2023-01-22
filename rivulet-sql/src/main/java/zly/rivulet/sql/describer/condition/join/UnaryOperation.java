package zly.rivulet.sql.describer.condition.join;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.describer.condition.ConditionOperate;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;

public interface UnaryOperation {

    ConditionOperate getConditionOperate();
    
    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate());
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate());
    }

    default <F, C> JoinCondition<F, C> of(Param<C> value) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, value, this.getConditionOperate());
    }

    default <F, C> JoinCondition<F, C> of(SqlQueryMetaDesc<F, C> value) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, value, this.getConditionOperate());
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate());
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate());
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> value) {
        return new ConditionElement<>(checkCondition, value, this.getConditionOperate());
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> value) {
        return new ConditionElement<>(checkCondition, value, this.getConditionOperate());
    }
}
