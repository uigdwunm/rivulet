package zly.rivulet.sql.describer.condition;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.function.Function;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;

import java.util.Arrays;

public interface Condition<F, C> {
    BinaryOperation EqualTo = new BinaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.EQ;
        }
    };

    ConditionOperate getOperate();

    @SafeVarargs
    static <F> ConditionContainer<F, ?> and(Condition<F, ?>... items) {
        return new ConditionContainer.AND<>(Arrays.asList(items));
    }

    @SafeVarargs
    static <F> ConditionContainer<F, ?> or(Condition<F, ?>... items) {
        return new ConditionContainer.OR<>(Arrays.asList(items));
    }



    static <F, C> Condition<F, C> notNull(SingleValueElementDesc<F, C> leftElement, CheckCondition checkCondition) {
        return new ConditionElement<>(checkCondition, leftElement, ConditionOperate.NOT_NULL);
    }

    static <F, C> Condition<F, C> notNull(SingleValueElementDesc<F, C> leftElement) {
        return notNull(leftElement, CheckCondition.IS_TRUE);
    }

//    static <P extends SingleValueElementDesc<F, C>, F, C> Condition<F, C> innerEqualTo(P leftElement, P rightElement) {
//        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
//    }
//
//    static <P extends SingleValueElementDesc<F, C>, F, C> Condition<F, C> innerEqualTo(P leftElement, Param<C> rightElement) {
//        SingleValueElementDesc<F, C> rightElement1 = (SingleValueElementDesc<F, C>) rightElement;
//        return new Condition<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement1);
//    }


    static <F, C> Condition<F, C> between(FieldMapping<F, C> leftElement, Param<C> beforeElement, Param<C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    static <F, C> Condition<F, C> between(SingleValueElementDesc<F, C> leftElement, SingleValueElementDesc<F, C> beforeElement, SingleValueElementDesc<F, C> afterElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }
}
