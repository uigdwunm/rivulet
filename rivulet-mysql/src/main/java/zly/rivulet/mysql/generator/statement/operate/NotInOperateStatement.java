package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.NotInOperateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class NotInOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public NotInOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    protected int length() {
        return leftValue.getLengthOrCache() + 2 + rightValue.getLengthOrCache();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(leftValue);
        collector.append(Constant.NOT_IN);
        collector.leftBracket();
        collector.append(rightValue);
        collector.rightBracket();
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            NotInOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                NotInOperateDefinition notInOperateDefinition = (NotInOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.warmUp(notInOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SqlStatement rightStatement = sqlStatementFactory.warmUp(notInOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new NotInOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                NotInOperateDefinition notInOperateDefinition = (NotInOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.getOrCreate(notInOperateDefinition.getLeftElement(), helper);
                SqlStatement rightStatement = sqlStatementFactory.getOrCreate(notInOperateDefinition.getRightElement(), helper);
                return new NotInOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}