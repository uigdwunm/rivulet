package zly.rivulet.sql.describer.query.desc;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;

public class Condition<F, C> extends AbstractCondition<F, F, C> {

    @SafeVarargs
    private Condition(CheckCondition checkCondition, SingleValueElementDesc<F, C> leftFieldMapped, ConditionOperate operate, SingleValueElementDesc<F, C> ... rightFieldMappeds) {
        super(checkCondition, leftFieldMapped, operate, rightFieldMappeds);
    }

    public SingleValueElementDesc<F, C> getLeftFieldMapped() {
        return super.getLeftFieldMapped();
    }

    public SingleValueElementDesc<F, C>[] getRightFieldMappeds() {
        return super.getRightFieldMappeds();
    }

    public static <F, C> Condition<F, C> notNull(SingleValueElementDesc<F, C> leftElement, CheckCondition checkCondition) {
        return new Condition<>(checkCondition, leftElement, ConditionOperate.NOT_NULL);
    }

    public static <F, C> Condition<F, C> notNull(SingleValueElementDesc<F, C> leftElement) {
        return notNull(leftElement, CheckCondition.IS_TRUE);
    }

    private static <P extends SingleValueElementDesc<F, C>, F, C> Condition<F, C> innerEqualTo(P leftElement, P rightElement) {
        return new Condition<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }

    private static <P extends SingleValueElementDesc<F, C>, F, C> Condition<F, C> innerEqualTo(P leftElement, Param<C> rightElement) {
        SingleValueElementDesc<F, C> rightElement1 = (SingleValueElementDesc<F, C>) rightElement;
        return new Condition<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement1);
    }

    public static <F, C> Condition<F, C> equalTo(FieldMapping<F, C> leftElement, FieldMapping<F, C> rightElement) {
        return innerEqualTo(leftElement, rightElement);
    }

    public static <F, C> Condition<F, C> equalTo(FieldMapping<F, C> leftElement, Param<C> rightElement) {
        return innerEqualTo(leftElement, rightElement);
    }

    public static <F, C> Condition<F, C> between(SingleValueElementDesc<F, C> leftElement, SingleValueElementDesc<F, C> beforeElement, SingleValueElementDesc<F, C> afterElement) {
        return new Condition<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }
}
