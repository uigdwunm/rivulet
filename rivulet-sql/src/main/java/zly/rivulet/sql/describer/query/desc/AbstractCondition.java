package zly.rivulet.sql.describer.query.desc;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;

public abstract class AbstractCondition<LF, RF, C> {
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
    public AbstractCondition(CheckCondition checkCondition, SingleValueElementDesc<LF, C> leftFieldMapped, ConditionOperate operate, SingleValueElementDesc<RF, C> ... rightFieldMappeds) {
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

}
