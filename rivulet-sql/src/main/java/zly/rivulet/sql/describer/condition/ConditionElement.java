package zly.rivulet.sql.describer.condition;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.condition.common.Condition;

public class ConditionElement implements Condition {

    private final SingleValueElementDesc leftFieldMapped;

    private final ConditionOperate operate;

    private final SingleValueElementDesc[] rightFieldMappeds;

    /**
     * Description 检测存在条件
     *
     * @author zhaolaiyuan
     * Date 2022/4/16 20:50
     **/
    private final CheckCondition checkCondition;

//    @SafeVarargs
//    public ConditionElement(CheckCondition checkCondition, SingleValueElementDesc leftFieldMapped, ConditionOperate operate, SingleValueElementDesc ... rightFieldMappeds) {
//        this.checkCondition = checkCondition;
//        this.leftFieldMapped = leftFieldMapped;
//        this.operate = operate;
//        this.rightFieldMappeds = rightFieldMappeds;
//    }

    public ConditionElement(CheckCondition checkCondition, Object leftFieldMapped, ConditionOperate operate) {
        this.checkCondition = checkCondition;
        this.leftFieldMapped = (SingleValueElementDesc) leftFieldMapped;
        this.operate = operate;
        this.rightFieldMappeds = new SingleValueElementDesc[]{};
    }

    public ConditionElement(CheckCondition checkCondition, Object leftFieldMapped, ConditionOperate operate, Object rightFieldMapped) {
        this.checkCondition = checkCondition;
        this.leftFieldMapped = (SingleValueElementDesc) leftFieldMapped;
        this.operate = operate;
        SingleValueElementDesc rightFieldMapped1 = (SingleValueElementDesc) rightFieldMapped;
        this.rightFieldMappeds = new SingleValueElementDesc[]{rightFieldMapped1};
    }

    public ConditionElement(CheckCondition checkCondition, Object leftFieldMapped, ConditionOperate operate, Object rightFieldMapped1, Object rightFieldMapped2) {
        this.checkCondition = checkCondition;
        this.leftFieldMapped = (SingleValueElementDesc) leftFieldMapped;
        this.operate = operate;
        SingleValueElementDesc rightFieldMapped11 = (SingleValueElementDesc) rightFieldMapped1;
        SingleValueElementDesc rightFieldMapped22 = (SingleValueElementDesc) rightFieldMapped2;
        this.rightFieldMappeds = new SingleValueElementDesc[]{rightFieldMapped11, rightFieldMapped22};
    }

    public CheckCondition getCheckCondition() {
        return checkCondition;
    }

    public SingleValueElementDesc getLeftFieldMapped() {
        return leftFieldMapped;
    }

    @Override
    public ConditionOperate getOperate() {
        return operate;
    }

    public SingleValueElementDesc[] getRightFieldMappeds() {
        return rightFieldMappeds;
    }
}
