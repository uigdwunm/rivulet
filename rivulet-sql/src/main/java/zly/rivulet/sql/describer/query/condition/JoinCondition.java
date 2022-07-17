package zly.rivulet.sql.describer.query.condition;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.param.Param;

import java.util.Arrays;

public interface JoinCondition<F, C> extends Condition<F, C> {

    static JoinConditionContainer<?, ?> and(JoinCondition<?, ?>... items) {
        return new JoinConditionContainer.AND<>(Arrays.asList(items));
    }

    static JoinConditionContainer<?, ?> or(JoinCondition<?, ?>... items) {
        return new JoinConditionContainer.OR<>(Arrays.asList(items));
    }

    static <F, C> JoinCondition<F, C> notNull(SingleValueElementDesc<F, C> leftElement, CheckCondition checkCondition) {
        return new ConditionElement<>(checkCondition, leftElement, ConditionOperate.NOT_NULL);
    }

    public static <F, C> JoinCondition<F, C> notNull(SingleValueElementDesc<F, C> leftElement) {
        return notNull(leftElement, CheckCondition.IS_TRUE);
    }



    static <C> JoinCondition<?, C> equalTo(JoinFieldMapping<C> leftElement, JoinFieldMapping<C> rightElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
//        return ConditionElement.equalTo(leftElement, rightElement);
    }

    static <C> JoinCondition<?, C> equalTo(JoinFieldMapping<C> leftElement, Param<C> rightElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
//        return ConditionElement.equalTo(leftElement, rightElement);
    }

//    public static <F, C> JoinCondition<F, C> between(SingleValueElementDesc<F, C> leftElement, SingleValueElementDesc<F, C> beforeElement, SingleValueElementDesc<F, C> afterElement) {
//        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, beforeElement, afterElement);
//    }
}
