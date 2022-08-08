package zly.rivulet.mysql.assembly_line.statement.query;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.assembly_line.statement.operate.OperateStatement;
import zly.rivulet.sql.definition.query.main.WhereDefinition;
import zly.rivulet.sql.assembly_line.SqlStatementFactory;
import zly.rivulet.sql.assembly_line.statement.SqlStatement;

public class WhereStatement implements SqlStatement {

    private final OperateStatement operateStatement;

    public static final String WHERE = "WHERE ";

    public WhereStatement(OperateStatement operateStatement) {
        this.operateStatement = operateStatement;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(WHERE);
        operateStatement.collectStatement(collector);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            WhereDefinition.class,
            (definition, soleFlag, initHelper) -> {
                WhereDefinition whereDefinition = (WhereDefinition) definition;
                OperateStatement operateStatement = (OperateStatement) sqlStatementFactory.init(whereDefinition.getOperateDefinition(), soleFlag.subSwitch(), initHelper);
                return new WhereStatement(operateStatement);
            },
            (definition, helper) -> {
                WhereDefinition whereDefinition = (WhereDefinition) definition;
                OperateStatement operateStatement = (OperateStatement) sqlStatementFactory.getOrCreate(whereDefinition.getOperateDefinition(), helper);
                return new WhereStatement(operateStatement);
            }
        );
    }
}
