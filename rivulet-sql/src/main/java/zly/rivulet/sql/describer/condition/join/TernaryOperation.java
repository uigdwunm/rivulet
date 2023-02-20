package zly.rivulet.sql.describer.condition.join;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.describer.condition.ConditionOperate;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;

public interface TernaryOperation {

    ConditionOperate getConditionOperate();
    
    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, JoinFieldMapping<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, JoinFieldMapping<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, JoinFieldMapping<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, JoinFieldMapping<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, SQLFunction<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, SQLFunction<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, SQLFunction<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, SQLFunction<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, Param<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, Param<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, Param<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(JoinFieldMapping<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, JoinFieldMapping<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, JoinFieldMapping<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, JoinFieldMapping<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, JoinFieldMapping<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, SQLFunction<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, SQLFunction<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, SQLFunction<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, SQLFunction<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, Param<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, Param<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, Param<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLFunction<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, JoinFieldMapping<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, JoinFieldMapping<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, JoinFieldMapping<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, JoinFieldMapping<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, SQLFunction<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, SQLFunction<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, SQLFunction<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, SQLFunction<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, Param<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, Param<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, Param<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(Param<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, JoinFieldMapping<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, JoinFieldMapping<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, JoinFieldMapping<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, JoinFieldMapping<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, SQLFunction<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, SQLFunction<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, SQLFunction<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, SQLFunction<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(SQLQueryMetaDesc<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, JoinFieldMapping<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, JoinFieldMapping<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, JoinFieldMapping<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, JoinFieldMapping<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, SQLFunction<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, SQLFunction<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, SQLFunction<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, SQLFunction<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, Param<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, Param<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, Param<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, JoinFieldMapping<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, JoinFieldMapping<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, JoinFieldMapping<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, JoinFieldMapping<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, JoinFieldMapping<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, SQLFunction<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, SQLFunction<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, SQLFunction<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, SQLFunction<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, Param<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, Param<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, Param<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLFunction<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, JoinFieldMapping<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, JoinFieldMapping<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, JoinFieldMapping<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, JoinFieldMapping<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SQLFunction<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SQLFunction<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SQLFunction<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SQLFunction<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Param<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Param<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, Param<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, Param<C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, JoinFieldMapping<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, JoinFieldMapping<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, JoinFieldMapping<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, JoinFieldMapping<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, SQLFunction<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, SQLFunction<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, SQLFunction<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, SQLFunction<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, Param<C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, JoinFieldMapping<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLFunction<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }

    default <F, C> JoinCondition<F, C> of(CheckCondition checkCondition, SQLQueryMetaDesc<F, C> leftElement, SQLQueryMetaDesc<F, C> beforeElement, SQLQueryMetaDesc<F, C> afterElement) {
        return new ConditionElement<>(checkCondition, leftElement, this.getConditionOperate(), beforeElement, afterElement);
    }
}
