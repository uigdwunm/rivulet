package pers.zly.base.definition;

import pers.zly.base.definition.checkCondition.CheckCondition;
import pers.zly.base.runparser.ParamManager;
import pers.zly.base.utils.RelationSwitch;

/**
 * Description 所有普通节点（非组节点）可以继承的抽象Defintion类
 *
 * @author zhaolaiyuan
 * Date 2022/1/22 10:51
 **/
public abstract class AbstractDefinition implements Definition, Checked {

    /**
     * 当前节点是否需要检查
     **/
    private final RelationSwitch needCheck;

    /**
     * 检测这个语句是否需要存在，为空表示不需要检测必须存在
     **/
    private final CheckCondition checkCondition;

    protected AbstractDefinition(RelationSwitch needCheck, CheckCondition checkCondition) {
        this.needCheck = needCheck;
        this.checkCondition = checkCondition;
    }

    @Override
    public boolean check(ParamManager paramManager) {
        if (!this.isNeedCheck()) {
            return true;
        }

        return checkCondition.checkCondition(paramManager);
    }

    /**
     * Description 是否需要校验
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 13:38
     **/
    public boolean isNeedCheck() {
        return checkCondition != null && CheckCondition.IS_TRUE != checkCondition;
    }

    /**
     * Description 是否可以缓存statement，基本都是true，除非需要拼接param，或者需要排序的
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 11:51
     **/
    public boolean canCatchStatement() {
        return true;
    }
}
