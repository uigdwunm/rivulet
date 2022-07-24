package zly.rivulet.mysql.runparser.statement.param;

import zly.rivulet.base.preparser.param.EmptyParamDefinition;
import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.mysql.runparser.statement.SingleValueElementStatement;
import zly.rivulet.sql.runparser.SqlStatementFactory;

public class EmptyParamStatement implements SingleValueElementStatement {

    @Override
    public String createStatement() {
        return null;
    }

    @Override
    public void collectStatement(StringBuilder sqlCollector) {

    }

    @Override
    public void formatGetStatement(FormatCollector formatCollector) {

    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            EmptyParamDefinition.class,
            (definition, soleFlag, initHelper) -> new EmptyParamStatement(),
            (definition, helper) -> new EmptyParamStatement()
        );
    }
}
