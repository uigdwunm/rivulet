package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.condition.join.JoinConditionContainer;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AndOperateDefinition extends OperateDefinition {

    private final List<OperateDefinition> operateDefinitionList;

    private AndOperateDefinition(CheckCondition checkCondition, List<OperateDefinition> operateDefinitionList) {
        super(checkCondition, null);
        this.operateDefinitionList = operateDefinitionList;
    }

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
    public Copier copier() {
        return new Copier(this.operateDefinitionList);
    }

    public class Copier implements Definition.Copier {

        private List<OperateDefinition> operateDefinitionList;

        public Copier(List<OperateDefinition> operateDefinitionList) {
            this.operateDefinitionList = operateDefinitionList;
        }

        public void setOperateDefinitionList(List<OperateDefinition> operateDefinitionList) {
            this.operateDefinitionList = operateDefinitionList;
        }

        @Override
        public AndOperateDefinition copy() {
            return new AndOperateDefinition(checkCondition, operateDefinitionList);
        }
    }
}
