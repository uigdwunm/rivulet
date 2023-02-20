package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.NotEqOperateDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class NotEqOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public NotEqOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public int length() {
        return leftValue.singleLength() + 1 + rightValue.singleLength();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        leftValue.singleCollectStatement(collector);
        collector.append(Constant.NOT_EQ);
        rightValue.singleCollectStatement(collector);
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            NotEqOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                NotEqOperateDefinition notEqOperateDefinition = (NotEqOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.warmUp(notEqOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SQLStatement rightStatement = sqlStatementFactory.warmUp(notEqOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new NotEqOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                NotEqOperateDefinition notEqOperateDefinition = (NotEqOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.getOrCreate(notEqOperateDefinition.getLeftElement(), helper);
                SQLStatement rightStatement = sqlStatementFactory.getOrCreate(notEqOperateDefinition.getRightElement(), helper);
                return new NotEqOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
