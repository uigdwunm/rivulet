package zly.rivulet.base.definition;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.utils.RelationSwitch;

import java.util.List;

public abstract class AbstactContainerDefinition extends AbstractDefinition {

    /**
     * 这个组是否需要排序
     **/
    private boolean needSort;

    private List<Definition> definitionList;

    protected AbstactContainerDefinition(RelationSwitch needCheck, CheckCondition checkCondition) {
        super(needCheck, checkCondition);
    }
}
