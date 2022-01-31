package pers.zly.sql.runparser.statement;

import pers.zly.base.runparser.statement.Statement;
import pers.zly.sql.definition.function.MFunctionDefinition;

public class FunctionStatement implements Statement {

    private String functionName;

    private SingleValueStatement singleValueStatement;
}
