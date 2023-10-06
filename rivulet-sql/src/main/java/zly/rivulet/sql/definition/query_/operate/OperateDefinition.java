package zly.rivulet.sql.definition.query_.operate;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.parser.ParamReceiptManager;

public abstract class OperateDefinition extends AbstractDefinition {
    protected OperateDefinition(CheckCondition checkCondition, ParamReceiptManager paramReceiptManager) {
        super(checkCondition, paramReceiptManager);
    }

}
