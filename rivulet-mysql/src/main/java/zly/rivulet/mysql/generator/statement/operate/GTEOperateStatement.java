package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query_.operate.GTEOperateDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class GTEOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public GTEOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
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
        collector.append(Constant.GTE);
        rightValue.singleCollectStatement(collector);
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            GTEOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                GTEOperateDefinition gteOperateDefinition = (GTEOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.warmUp(gteOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SQLStatement rightStatement = sqlStatementFactory.warmUp(gteOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new GTEOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                GTEOperateDefinition gteOperateDefinition = (GTEOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.getOrCreate(gteOperateDefinition.getLeftElement(), helper);
                SQLStatement rightStatement = sqlStatementFactory.getOrCreate(gteOperateDefinition.getRightElement(), helper);
                return new GTEOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
