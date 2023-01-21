package zly.rivulet.sql.describer.condition;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.join.JoinCondition;

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

//    @SafeVarargs
//    public ConditionElement(CheckCondition checkCondition, SingleValueElementDesc<F, C> leftFieldMapped, ConditionOperate operate, SingleValueElementDesc<F, C> ... rightFieldMappeds) {
//        this.checkCondition = checkCondition;
//        this.leftFieldMapped = leftFieldMapped;
//        this.operate = operate;
//        this.rightFieldMappeds = rightFieldMappeds;
//    }

    public ConditionElement(CheckCondition checkCondition, Object leftFieldMapped, ConditionOperate operate) {
        this.checkCondition = checkCondition;
        this.leftFieldMapped = (SingleValueElementDesc<F, C>) leftFieldMapped;
        this.operate = operate;
        this.rightFieldMappeds = new SingleValueElementDesc[]{};
    }

    public ConditionElement(CheckCondition checkCondition, Object leftFieldMapped, ConditionOperate operate, Object rightFieldMapped) {
        this.checkCondition = checkCondition;
        this.leftFieldMapped = (SingleValueElementDesc<F, C>) leftFieldMapped;
        this.operate = operate;
        SingleValueElementDesc<F, C> rightFieldMapped1 = (SingleValueElementDesc<F, C>) rightFieldMapped;
        this.rightFieldMappeds = new SingleValueElementDesc[]{rightFieldMapped1};
    }

    public ConditionElement(CheckCondition checkCondition, Object leftFieldMapped, ConditionOperate operate, Object rightFieldMapped1, Object rightFieldMapped2) {
        this.checkCondition = checkCondition;
        this.leftFieldMapped = (SingleValueElementDesc<F, C>) leftFieldMapped;
        this.operate = operate;
        SingleValueElementDesc<F, C> rightFieldMapped11 = (SingleValueElementDesc<F, C>) rightFieldMapped1;
        SingleValueElementDesc<F, C> rightFieldMapped22 = (SingleValueElementDesc<F, C>) rightFieldMapped2;
        this.rightFieldMappeds = new SingleValueElementDesc[]{rightFieldMapped11, rightFieldMapped22};
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
}
