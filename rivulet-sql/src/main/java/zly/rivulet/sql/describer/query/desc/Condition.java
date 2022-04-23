package zly.rivulet.sql.describer.query.desc;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;

import java.util.ArrayList;
import java.util.List;

public class Condition<LF, RF, C> {
    private final SingleValueElementDesc<LF, C> leftFieldMapped;
    private final ConditionOperate operate;
    private final SingleValueElementDesc<RF, C>[] rightFieldMappeds;

    /**
     * Description 检测存在条件
     *
     * @author zhaolaiyuan
     * Date 2022/4/16 20:50
     **/
    private final CheckCondition checkCondition;

    @SafeVarargs
    public Condition(CheckCondition checkCondition, SingleValueElementDesc<LF, C> leftFieldMapped, ConditionOperate operate, SingleValueElementDesc<RF, C> ... rightFieldMappeds) {
        this.checkCondition = checkCondition;
        this.leftFieldMapped = leftFieldMapped;
        this.operate = operate;
        this.rightFieldMappeds = rightFieldMappeds;
    }

    public CheckCondition getCheckCondition() {
        return checkCondition;
    }

    public SingleValueElementDesc<LF, C> getLeftFieldMapped() {
        return leftFieldMapped;
    }

    public ConditionOperate getOperate() {
        return operate;
    }

    public SingleValueElementDesc<RF, C>[] getRightFieldMappeds() {
        return rightFieldMappeds;
    }


    public static <F, C> Condition<F, F, C> isNull(SingleValueElementDesc<F, C> leftElement) {
        return isNull(leftElement, CheckCondition.IS_TRUE);
    }

    public static <F, C> Condition<F, F, C> isNull(SingleValueElementDesc<F, C> leftElement, CheckCondition checkCondition) {
        return new Condition<>(checkCondition, leftElement, ConditionOperate.IS_NULL);
    }

    public static <F, C> Condition<F, F, C> notNull(SingleValueElementDesc<F, C> leftElement) {
        return new Condition<>(leftElement, ConditionOperate.NOT_NULL);
    }
    public static class EqualTo<F, C> {

        public static <A extends SingleValueElementDesc<F, C>, B extends SingleValueElementDesc<F, C>, F, C> Condition<F, F, C> equalTo1(A leftElement, B rightElement) {
            return new Condition<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
        }
    }

    public static <D> List<D> tt() {
        return new ArrayList<>();

    }

    public static <D> List<D> tt1() {
        return tt();
    }


    public static <A extends SingleValueElementDesc<F, C>, B extends SingleValueElementDesc<F, C>, F, C> Condition<F, F, C> equalTo(A leftElement, B rightElement) {
        return new Condition<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }

    public static <A extends SingleValueElementDesc<F, C>, F, C> Condition<F, F, C> equalTo(A leftElement, Param<C> rightElement) {
        return new Condition<>(CheckCondition.IS_TRUE, leftElement, ConditionOperate.EQ, rightElement);
    }

    public static <F, C> Condition<F, F, C> equalTo(FieldMapping<F, C> leftElement, FieldMapping<F, C> rightElement) {
        return equalTo(leftElement, rightElement);
    }

    public static <F, C> Condition<F, F, C> equalTo(FieldMapping<F, C> leftElement, Param<C> rightElement) {
        return equalTo(leftElement, rightElement);
    }

    public static <F, C> Condition<F, F, C> between(SingleValueElementDesc<F, C> leftElement, SingleValueElementDesc<F, C> beforeElement, SingleValueElementDesc<F, C> afterElement) {
        return new Condition<>(leftElement, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }
}
