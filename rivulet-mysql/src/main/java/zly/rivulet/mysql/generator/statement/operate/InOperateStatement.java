package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.InOperateDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class InOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public InOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public int length() {
        return leftValue.singleLength() + Constant.IN.length() + 2 + rightValue.getLengthOrCache();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        leftValue.singleLength();
        collector.append(Constant.IN);
        collector.leftBracket();
        collector.append(rightValue);
        collector.rightBracket();
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            InOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                InOperateDefinition inOperateDefinition = (InOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.warmUp(inOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SQLStatement rightStatement = sqlStatementFactory.warmUp(inOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new InOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                InOperateDefinition inOperateDefinition = (InOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.getOrCreate(inOperateDefinition.getLeftElement(), helper);
                SQLStatement rightStatement = sqlStatementFactory.getOrCreate(inOperateDefinition.getRightElement(), helper);
                return new InOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
