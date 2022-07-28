package zly.rivulet.mysql.runparser.statement.param;

import zly.rivulet.base.preparser.param.EmptyParamDefinition;
import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.base.utils.StatementCollector;
import zly.rivulet.mysql.runparser.statement.SingleValueElementStatement;
import zly.rivulet.sql.runparser.SqlStatementFactory;

public class EmptyParamStatement implements SingleValueElementStatement {

    private static final char questionMark = '?';

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(questionMark);
    }

    @Override
    public void formatGetStatement(FormatCollector collector) {
        collector.append(questionMark);

    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            EmptyParamDefinition.class,
            (definition, soleFlag, initHelper) -> new EmptyParamStatement(),
            (definition, helper) -> new EmptyParamStatement()
        );
    }
}
