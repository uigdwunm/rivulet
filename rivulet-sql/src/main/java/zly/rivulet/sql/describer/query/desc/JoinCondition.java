package zly.rivulet.sql.describer.query.desc;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.param.Param;

public class JoinCondition<LF, RF, C> extends AbstractCondition<LF, RF, C> {

    @SafeVarargs
    private JoinCondition(CheckCondition checkCondition, SingleValueElementDesc<LF, C> leftFieldMapped, ConditionOperate operate, SingleValueElementDesc<RF, C> ... rightFieldMappeds) {
        super(checkCondition, leftFieldMapped, operate, rightFieldMappeds);
    }

    public SingleValueElementDesc<LF, C> getLeftFieldMapped() {
        return super.getLeftFieldMapped();
    }

    public SingleValueElementDesc<RF, C>[] getRightFieldMappeds() {
        return super.getRightFieldMappeds();
    }

    public static <F, C> JoinCondition<F, F, C> notNull(SingleValueElementDesc<F, C> leftElement, CheckCondition checkCondition) {
        return new JoinCondition<>(checkCondition, leftElement, ConditionOperate.NOT_NULL);
    }

    public static <F, C> JoinCondition<F, F, C> notNull(SingleValueElementDesc<F, C> leftElement) {
        return notNull(leftElement, CheckCondition.IS_TRUE);
    }

    private static <A extends SingleValueElementDesc<LF, C>, B extends SingleValueElementDesc<RF, C>, LF, RF, C> JoinCondition<LF, RF, C> innerEqualTo(A leftElement, B rightElement) {
        return new JoinCondition<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }

    private static <A extends SingleValueElementDesc<F, C>, F, C> JoinCondition<F, ?, C> innerEqualTo(A leftElement, Param<C> rightElement) {
        return new JoinCondition<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }

    public static <C> JoinCondition<?, ?, C> equalTo(JoinFieldMapping<C> leftElement, JoinFieldMapping<C> rightElement) {
        return innerEqualTo(leftElement, rightElement);
    }

    public static <C> JoinCondition<?, ?, C> equalTo(JoinFieldMapping<C> leftElement, Param<C> rightElement) {
        return innerEqualTo(leftElement, rightElement);
    }

    public static <F, C> JoinCondition<F, F, C> between(SingleValueElementDesc<F, C> leftElement, SingleValueElementDesc<F, C> beforeElement, SingleValueElementDesc<F, C> afterElement) {
        return new JoinCondition<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }
}
