package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.LTEOperateDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class LTEOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public LTEOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
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
        collector.append(Constant.LTE);
        rightValue.singleCollectStatement(collector);
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            LTEOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                LTEOperateDefinition lteOperateDefinition = (LTEOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.warmUp(lteOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SQLStatement rightStatement = sqlStatementFactory.warmUp(lteOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new LTEOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                LTEOperateDefinition lteOperateDefinition = (LTEOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.getOrCreate(lteOperateDefinition.getLeftElement(), helper);
                SQLStatement rightStatement = sqlStatementFactory.getOrCreate(lteOperateDefinition.getRightElement(), helper);
                return new LTEOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
