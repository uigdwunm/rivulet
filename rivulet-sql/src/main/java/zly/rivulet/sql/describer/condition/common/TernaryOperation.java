package zly.rivulet.sql.describer.condition.common;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.describer.condition.ConditionOperate;
import zly.rivulet.sql.describer.function.Function;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;

public interface TernaryOperation {

    ConditionOperate getConditionOperate();
    
    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, FieldMapping<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, FieldMapping<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, FieldMapping<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, FieldMapping<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, Function<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, Function<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, Function<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, Function<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, Param<C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, Param<C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, Param<C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(FieldMapping<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, FieldMapping<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, FieldMapping<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, FieldMapping<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, FieldMapping<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, Function<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, Function<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, Function<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, Function<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, Param<C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, Param<C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, Param<C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Function<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, FieldMapping<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, FieldMapping<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, FieldMapping<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, FieldMapping<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, Function<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, Function<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, Function<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, Function<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, Param<C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, Param<C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, Param<C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(Param<C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, FieldMapping<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, FieldMapping<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, FieldMapping<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, FieldMapping<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, Function<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, Function<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, Function<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, Function<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(SqlQueryMetaDesc<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, FieldMapping<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, FieldMapping<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, FieldMapping<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, FieldMapping<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, Function<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, Function<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, Function<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, Function<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, Param<C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, Param<C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, Param<C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, FieldMapping<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, FieldMapping<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, FieldMapping<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, FieldMapping<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, FieldMapping<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, Function<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, Function<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, Function<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, Function<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, Param<C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, Param<C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, Param<C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Function<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, FieldMapping<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, FieldMapping<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, FieldMapping<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, FieldMapping<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Function<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Function<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Function<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Function<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Param<C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Param<C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Param<C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, FieldMapping<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, FieldMapping<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, FieldMapping<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, FieldMapping<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, Function<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, Function<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, Function<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, Function<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, FieldMapping<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Function<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> Condition<F, C> of(CheckCondition checkCondition, SqlQueryMetaDesc<F, C> leftElement, SqlQueryMetaDesc<F, C> beforeElement, SqlQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }
}
