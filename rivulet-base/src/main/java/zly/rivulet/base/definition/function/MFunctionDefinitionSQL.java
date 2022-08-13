package zly.rivulet.base.definition.function;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.generator.statement.Statement;

public abstract class MFunctionDefinitionSQL implements SingleValueElementDefinition {

    private Class<?> returnType;

    public abstract Statement toStatement(Object[] originParam);
}
