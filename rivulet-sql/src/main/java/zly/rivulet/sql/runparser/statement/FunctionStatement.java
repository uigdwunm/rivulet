package zly.rivulet.sql.runparser.statement;

import zly.rivulet.base.runparser.statement.Statement;

public class FunctionStatement implements Statement {

    private String functionName;

    private SingleValueStatement singleValueStatement;
}
