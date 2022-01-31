package pers.zly.sql.definition.function;

import pers.zly.base.runparser.statement.Statement;

public abstract class MFunctionDefinition {

    private Class<?> returnType;

    public abstract Statement toStatement(Object[] originParam);
}
