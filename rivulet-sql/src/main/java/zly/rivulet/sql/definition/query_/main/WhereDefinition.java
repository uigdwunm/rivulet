package zly.rivulet.sql.definition.query_.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definition.query_.operate.OperateDefinition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.parser.toolbox_.SQLParserPortableToolbox;

public class WhereDefinition extends AbstractContainerDefinition {

    private final OperateDefinition operateDefinition;

    private WhereDefinition(CheckCondition checkCondition, OperateDefinition operateDefinition) {
        super(checkCondition, null);
        this.operateDefinition = operateDefinition;
    }

    public WhereDefinition(SQLParserPortableToolbox sqlPreParseHelper, ConditionContainer<?, ?> whereConditionContainer) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamReceiptManager());
        this.operateDefinition = whereConditionContainer.getOperate().createDefinition(sqlPreParseHelper, whereConditionContainer);
    }

    public WhereDefinition(SQLParserPortableToolbox sqlPreParseHelper, OperateDefinition operateDefinition) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamReceiptManager());
        this.operateDefinition = operateDefinition;
    }

    public OperateDefinition getOperateDefinition() {
        return operateDefinition;
    }

    @Override
    public Copier copier() {
        return new Copier(this.operateDefinition);
    }

    public class Copier implements Definition.Copier {

        private OperateDefinition operateDefinition;

        public Copier(OperateDefinition operateDefinition) {
            this.operateDefinition = operateDefinition;
        }

        public void setOperateDefinition(OperateDefinition operateDefinition) {
            this.operateDefinition = operateDefinition;
        }

        @Override
        public WhereDefinition copy() {
            return new WhereDefinition(checkCondition, operateDefinition);
        }
    }
}
