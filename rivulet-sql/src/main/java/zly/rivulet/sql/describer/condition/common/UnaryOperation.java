package zly.rivulet.sql.describer.condition.common;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.describer.condition.ConditionOperate;
import zly.rivulet.sql.describer.function.Function;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;

public interface UnaryOperation {

    ConditionOperate getConditionOperate();
    
    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate());
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate());
    }

    default <F, C> Condition<F, C> of(Param<C> value) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, value, this.getConditionOperate());
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> value) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, value, this.getConditionOperate());
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate());
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate());
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> value) {
        return new ConditionElement<>(checkCondition, value, this.getConditionOperate());
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> value) {
        return new ConditionElement<>(checkCondition, value, this.getConditionOperate());
    }
}
