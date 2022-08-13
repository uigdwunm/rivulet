package zly.rivulet.sql.generator.statement;

import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.utils.collector.StatementCollector;

public class FunctionStatement implements Statement {

    private String functionName;

    private SingleValueStatement singleValueStatement;

    @Override
    public void collectStatement(StatementCollector collector) {

    }
}
