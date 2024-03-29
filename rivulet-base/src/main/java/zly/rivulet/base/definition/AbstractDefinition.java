package zly.rivulet.base.definition;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.generator.param_manager.ParamManager;

/**
 * Description 所有普通节点（非组节点）可以继承的抽象Definition类
 *
 * @author zhaolaiyuan
 * Date 2022/1/22 10:51
 **/
public abstract class AbstractDefinition implements Definition, Checked {

    /**
     * 检测这个语句是否需要存在
     **/
    protected final CheckCondition checkCondition;

    protected AbstractDefinition(CheckCondition checkCondition, ParamReceiptManager paramReceiptManager) {
        if (paramReceiptManager != null) {
            checkCondition.registerParam(paramReceiptManager);
        }
        this.checkCondition = checkCondition;
    }

    @Override
    public boolean check(CommonParamManager paramManager) {
        if (CheckCondition.IS_TRUE == this.checkCondition) {
            // 稍微优化一下
            return true;
        }

        return checkCondition.checkCondition(paramManager);
    }

}
