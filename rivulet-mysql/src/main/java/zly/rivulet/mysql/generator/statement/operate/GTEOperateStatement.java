package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.GTEOperateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class GTEOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public GTEOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
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
        collector.append(Constant.GTE);
        collector.append(rightValue);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            GTEOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                GTEOperateDefinition gteOperateDefinition = (GTEOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.warmUp(gteOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SqlStatement rightStatement = sqlStatementFactory.warmUp(gteOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new GTEOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                GTEOperateDefinition gteOperateDefinition = (GTEOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.getOrCreate(gteOperateDefinition.getLeftElement(), helper);
                SqlStatement rightStatement = sqlStatementFactory.getOrCreate(gteOperateDefinition.getRightElement(), helper);
                return new GTEOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
