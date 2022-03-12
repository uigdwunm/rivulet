package zly.rivulet.sql.definition.function;

import zly.rivulet.base.runparser.statement.Statement;

public abstract class MFunctionDefinition {

    private Class<?> returnType;

    public abstract Statement toStatement(Object[] originParam);
}
