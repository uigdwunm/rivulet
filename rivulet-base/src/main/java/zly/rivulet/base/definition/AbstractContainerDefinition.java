package zly.rivulet.base.definition;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.parser.ParamReceiptManager;

public abstract class AbstractContainerDefinition extends AbstractDefinition {

    /**
     * 这个组是否需要排序
     **/
    private boolean needSort;

    protected AbstractContainerDefinition(CheckCondition checkCondition, ParamReceiptManager paramReceiptManager) {
        super(checkCondition, paramReceiptManager);
    }

}
