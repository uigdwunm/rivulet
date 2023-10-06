package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.operate.OperateStatement;
import zly.rivulet.sql.definition.query_.main.WhereDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class WhereStatement extends SQLStatement {

    private final OperateStatement operateStatement;

    public static final String WHERE = "WHERE ";

    public WhereStatement(OperateStatement operateStatement) {
        this.operateStatement = operateStatement;
    }

    @Override
    public int length() {
        return WHERE.length() + operateStatement.getLengthOrCache();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(WHERE);
        collector.append(operateStatement);
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            WhereDefinition.class,
            (definition, soleFlag, initHelper) -> {
                WhereDefinition whereDefinition = (WhereDefinition) definition;
                OperateStatement operateStatement = (OperateStatement) sqlStatementFactory.warmUp(whereDefinition.getOperateDefinition(), soleFlag.subSwitch(), initHelper);
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
