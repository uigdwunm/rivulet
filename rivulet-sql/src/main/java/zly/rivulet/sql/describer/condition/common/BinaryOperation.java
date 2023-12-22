package zly.rivulet.sql.describer.condition.common;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.describer.condition.ConditionOperate;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.definer.meta.SQLColumnMeta;
import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;

public interface BinaryOperation {

    ConditionOperate getConditionOperate();

    default Condition of(SQLColumnMeta<?> leftElement, SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(SQLColumnMeta<?> leftElement, SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(SQLColumnMeta<?> leftElement, Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(SQLColumnMeta<?> leftElement, SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(SQLFunction<?> leftElement, SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(SQLFunction<?> leftElement, SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(SQLFunction<?> leftElement, Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(SQLFunction<?> leftElement, SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(Param<?> leftElement, SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(Param<?> leftElement, SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(Param<?> leftElement, Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(Param<?> leftElement, SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(SQLQueryMetaDesc<?> leftElement, SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(SQLQueryMetaDesc<?> leftElement, SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(SQLQueryMetaDesc<?> leftElement, Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(SQLQueryMetaDesc<?> leftElement, SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLColumnMeta<?> leftElement, SQLColumnMeta<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLColumnMeta<?> leftElement, SQLFunction<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLColumnMeta<?> leftElement, Param<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLColumnMeta<?> leftElement, SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLFunction<?> leftElement, SQLColumnMeta<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLFunction<?> leftElement, SQLFunction<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLFunction<?> leftElement, Param<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLFunction<?> leftElement, SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, Param<?> leftElement, SQLColumnMeta<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, Param<?> leftElement, SQLFunction<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, Param<?> leftElement, Param<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, Param<?> leftElement, SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLQueryMetaDesc<?> leftElement, SQLColumnMeta<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLQueryMetaDesc<?> leftElement, SQLFunction<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLQueryMetaDesc<?> leftElement, Param<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }

    default Condition of(CheckCondition checkCondition, SQLQueryMetaDesc<?> leftElement, SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(checkCondition, leftElement, this.getConditionOperate(), rightElement);
    }
}
