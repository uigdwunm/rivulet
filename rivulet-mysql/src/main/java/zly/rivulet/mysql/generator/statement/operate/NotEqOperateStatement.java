package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.NotEqOperateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class NotEqOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public NotEqOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    protected int length() {
        return leftValue.getLengthOrCache() + 1 + rightValue.getLengthOrCache();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(leftValue);
        collector.append(Constant.NOT_EQ);
        collector.append(rightValue);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            NotEqOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                NotEqOperateDefinition notEqOperateDefinition = (NotEqOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.warmUp(notEqOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SqlStatement rightStatement = sqlStatementFactory.warmUp(notEqOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new NotEqOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                NotEqOperateDefinition notEqOperateDefinition = (NotEqOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.getOrCreate(notEqOperateDefinition.getLeftElement(), helper);
                SqlStatement rightStatement = sqlStatementFactory.getOrCreate(notEqOperateDefinition.getRightElement(), helper);
                return new NotEqOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
