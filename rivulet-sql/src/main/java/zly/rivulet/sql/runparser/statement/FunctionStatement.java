package zly.rivulet.sql.runparser.statement;

import zly.rivulet.base.assembly_line.statement.Statement;
import zly.rivulet.base.utils.collector.StatementCollector;

public class FunctionStatement implements Statement {

    private String functionName;

    private SingleValueStatement singleValueStatement;

    @Override
    public void collectStatement(StatementCollector collector) {

    }
}
