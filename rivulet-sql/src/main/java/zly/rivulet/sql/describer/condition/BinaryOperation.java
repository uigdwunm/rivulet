package zly.rivulet.sql.describer.condition;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.function.Function;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;

public interface BinaryOperation {

    ConditionOperate getConditionOperate();

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, FieldMapping<F, C> rightElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, Function<F, C> rightElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, Param<C> rightElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, Function<F, C> rightElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, Param<C> rightElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, SqlQueryMetaDesc<F, C> rightElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }


    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, FieldMapping<F, C> rightElement) {
        return new ConditionElement<>(checkCondition, leftElement, ConditionOperate.EQ, rightElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, Function<F, C> rightElement) {
        return new ConditionElement<>(checkCondition, leftElement, ConditionOperate.EQ, rightElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, Param<C> rightElement) {
        return new ConditionElement<>(checkCondition, leftElement, ConditionOperate.EQ, rightElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, Function<F, C> rightElement) {
        return new ConditionElement<>(checkCondition, leftElement, ConditionOperate.EQ, rightElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, Param<C> rightElement) {
        return new ConditionElement<>(checkCondition, leftElement, ConditionOperate.EQ, rightElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, SqlQueryMetaDesc<F, C> rightElement) {
        return new ConditionElement<>(checkCondition, leftElement, ConditionOperate.EQ, rightElement);
    }
}
