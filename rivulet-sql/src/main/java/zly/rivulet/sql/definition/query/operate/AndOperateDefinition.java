package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.condition.JoinConditionContainer;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AndOperateDefinition extends OperateDefinition {

    private final List<OperateDefinition> operateDefinitionList;

    public AndOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, Condition<?, ?> condition) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamReceiptManager());
        List<OperateDefinition> operateDefinitionList = new ArrayList<>();

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
        this.operateDefinitionList = operateDefinitionList;
    }

    public AndOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, OperateDefinition ... operateDefinitions) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamReceiptManager());
        this.operateDefinitionList = Stream.of(operateDefinitions).collect(Collectors.toList());
    }

    public List<OperateDefinition> getOperateDefinitionList() {
        return operateDefinitionList;
    }

    @Override
    public AndOperateDefinition forAnalyze() {
        return null;
    }
}
