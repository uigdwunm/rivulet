package zly.rivulet.sql.runparser.statement;

import zly.rivulet.base.runparser.statement.Statement;
import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.base.utils.StatementCollector;

public class FunctionStatement implements Statement {

    private String functionName;

    private SingleValueStatement singleValueStatement;

    @Override
    public void collectStatement(StatementCollector collector) {

    }

    @Override
    public void formatGetStatement(FormatCollector formatCollector) {

    }
}
