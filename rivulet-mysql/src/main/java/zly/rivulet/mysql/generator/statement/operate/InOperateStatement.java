package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.definition.query.operate.InOperateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class InOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public InOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
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
        collector.append(Constant.IN);
        collector.leftBracket();
        collector.append(rightValue);
        collector.rightBracket();
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            InOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                InOperateDefinition inOperateDefinition = (InOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.warmUp(inOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SqlStatement rightStatement = sqlStatementFactory.warmUp(inOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new InOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                InOperateDefinition inOperateDefinition = (InOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.getOrCreate(inOperateDefinition.getLeftElement(), helper);
                SqlStatement rightStatement = sqlStatementFactory.getOrCreate(inOperateDefinition.getRightElement(), helper);
                return new InOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
