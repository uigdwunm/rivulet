package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;

public abstract class OperateDefinition extends AbstractDefinition {
    protected OperateDefinition(CheckCondition checkCondition) {
        super(checkCondition);
    }
}
