package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.condition.JoinConditionContainer;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.ArrayList;
import java.util.List;

public class OrOperateDefinition extends OperateDefinition {

    private final List<OperateDefinition> operateDefinitionList;

    private OrOperateDefinition(CheckCondition checkCondition, List<OperateDefinition> operateDefinitionList) {
        super(checkCondition, null);
        this.operateDefinitionList =operateDefinitionList;
    }

    public OrOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, Condition<?, ?> condition) {
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

    public List<OperateDefinition> getOperateDefinitionList() {
        return operateDefinitionList;
    }

    @Override
    public Copier copier() {
        return new Copier(operateDefinitionList);
    }

    public class Copier implements Definition.Copier {

        private List<OperateDefinition> operateDefinitionList;

        private Copier(List<OperateDefinition> operateDefinitionList) {
            this.operateDefinitionList = operateDefinitionList;
        }

        public void setOperateDefinitionList(List<OperateDefinition> operateDefinitionList) {
            this.operateDefinitionList = operateDefinitionList;
        }

        @Override
        public Definition copy() {
            return new OrOperateDefinition(checkCondition, operateDefinitionList);
        }
    }
}
