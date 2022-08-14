package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.condition.JoinConditionContainer;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.ArrayList;
import java.util.List;

public class AndOperateDefinition extends OperateDefinition {

    private final ArrayList<OperateDefinition> operateDefinitionList = new ArrayList<>();

    public AndOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, Condition<?, ?> condition) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamDefinitionManager());
        List<OperateDefinition> operateDefinitionList = this.operateDefinitionList;

        if (condition instanceof ConditionContainer) {
            ConditionContainer<?, ?> container = (ConditionContainer<?, ?>) condition;
            for (Condition<?, ?> item : container.getConditionElementList()) {
                operateDefinitionList.add(item.getOperate().createDefinition(sqlPreParseHelper, item));
            }

        } else if (condition instanceof JoinConditionContainer) {
            JoinConditionContainer<?, ?> container = (JoinConditionContainer<?, ?>) condition;
            for (Condition<?, ?> item : container.getConditionElementList()) {
                operateDefinitionList.add(item.getOperate().createDefinition(sqlPreParseHelper, item));
            }
        } else {
            throw UnbelievableException.unknownType();
        }
    }

    public ArrayList<OperateDefinition> getOperateDefinitionList() {
        return operateDefinitionList;
    }

    @Override
    public AndOperateDefinition forAnalyze() {
        return null;
    }
}
