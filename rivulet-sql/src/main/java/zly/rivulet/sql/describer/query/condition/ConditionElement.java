package zly.rivulet.sql.describer.query.condition;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.param.Param;

public class ConditionElement<F, C> implements Condition<F, C>, JoinCondition<F, C> {

    private final SingleValueElementDesc<F, C> leftFieldMapped;

    private final ConditionOperate operate;

    private final SingleValueElementDesc<F, C>[] rightFieldMappeds;

    /**
     * Description 检测存在条件
     *
     * @author zhaolaiyuan
     * Date 2022/4/16 20:50
     **/
    private final CheckCondition checkCondition;

    @SafeVarargs
    public ConditionElement(CheckCondition checkCondition, SingleValueElementDesc<F, C> leftFieldMapped, ConditionOperate operate, SingleValueElementDesc<F, C> ... rightFieldMappeds) {
        this.checkCondition = checkCondition;
        this.leftFieldMapped = leftFieldMapped;
        this.operate = operate;
        this.rightFieldMappeds = rightFieldMappeds;
    }

    public CheckCondition getCheckCondition() {
        return checkCondition;
    }

    public SingleValueElementDesc<F, C> getLeftFieldMapped() {
        return leftFieldMapped;
    }

    @Override
    public ConditionOperate getOperate() {
        return operate;
    }

    public SingleValueElementDesc<F, C>[] getRightFieldMappeds() {
        return rightFieldMappeds;
    }

    public static <A extends SingleValueElementDesc<F, C>, F, C> ConditionElement<F, C> equalTo(A leftElement, A rightElement) {
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }

    public static <A extends SingleValueElementDesc<F, C>, F, C> ConditionElement<F, C> equalTo(A leftElement, Param<C> rightElement) {
        SingleValueElementDesc<F, C> rightElement1 = (SingleValueElementDesc<F, C>) rightElement;
        return new ConditionElement<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement1);
    }
}
