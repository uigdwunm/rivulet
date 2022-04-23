package zly.rivulet.base.definition;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.preparser.ParamDefinitionManager;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainerDefinition extends AbstractDefinition {

    /**
     * 这个组是否需要排序
     **/
    private boolean needSort;

    protected AbstractContainerDefinition(CheckCondition checkCondition, ParamDefinitionManager paramDefinitionManager) {
        super(checkCondition, paramDefinitionManager);
    }

    public abstract ArrayList<?> getList();
}
