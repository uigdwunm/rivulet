package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.describer.query.condition.ConditionContainer;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class WhereDefinition extends AbstractContainerDefinition {

    private final OperateDefinition operateDefinition;

    private WhereDefinition(OperateDefinition operateDefinition) {
        super(CheckCondition.IS_TRUE, null);
        this.operateDefinition = operateDefinition;
    }

    public WhereDefinition(SqlParserPortableToolbox sqlPreParseHelper, ConditionContainer<?, ?> whereConditionContainer) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamDefinitionManager());
        this.operateDefinition = whereConditionContainer.getOperate().createDefinition(sqlPreParseHelper, whereConditionContainer);
    }

    public OperateDefinition getOperateDefinition() {
        return operateDefinition;
    }

    @Override
    public WhereDefinition forAnalyze() {
        return new WhereDefinition(operateDefinition.forAnalyze());
    }

}
