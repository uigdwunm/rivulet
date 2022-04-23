package zly.rivulet.base.definition.function;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.runparser.statement.Statement;

public abstract class MFunctionDefinition implements SingleValueElementDefinition {

    private Class<?> returnType;

    public abstract Statement toStatement(Object[] originParam);
}
