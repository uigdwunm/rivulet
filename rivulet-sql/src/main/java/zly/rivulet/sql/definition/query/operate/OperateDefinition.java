package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;

public abstract class OperateDefinition extends AbstractDefinition {
    protected OperateDefinition(CheckCondition checkCondition, ParamDefinitionManager paramDefinitionManager) {
        super(checkCondition, paramDefinitionManager);
    }

    @Override
    public abstract OperateDefinition forAnalyze();
}
