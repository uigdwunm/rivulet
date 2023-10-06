package zly.rivulet.sql.definition.query_.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definition.query_.operate.OperateDefinition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.parser.toolbox_.SQLParserPortableToolbox;

public class HavingDefinition extends AbstractDefinition {

    private final OperateDefinition operateDefinition;

    private HavingDefinition(CheckCondition checkCondition, OperateDefinition operateDefinition) {
        super(checkCondition, null);
        this.operateDefinition = operateDefinition;
    }

    public HavingDefinition(SQLParserPortableToolbox toolbox, ConditionContainer<?, ?> havingConditionContainer) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        this.operateDefinition = havingConditionContainer.getOperate().createDefinition(toolbox, havingConditionContainer);
    }

    @Override
    public Copier copier() {
        return new Copier(this.operateDefinition);
    }

    public class Copier implements Definition.Copier {

        private OperateDefinition operateDefinition;

        private Copier(OperateDefinition operateDefinition) {
            this.operateDefinition = operateDefinition;
        }

        public void setOperateDefinition(OperateDefinition operateDefinition) {
            this.operateDefinition = operateDefinition;
        }

        @Override
        public HavingDefinition copy() {
            return new HavingDefinition(checkCondition, operateDefinition);
        }
    }
}
