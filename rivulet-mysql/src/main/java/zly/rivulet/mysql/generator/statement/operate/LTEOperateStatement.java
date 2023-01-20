package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.GTEOperateDefinition;
import zly.rivulet.sql.definition.query.operate.LTEOperateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class LTEOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public LTEOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
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
        collector.append(Constant.LTE);
        collector.append(rightValue);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            LTEOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                LTEOperateDefinition lteOperateDefinition = (LTEOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.warmUp(lteOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SqlStatement rightStatement = sqlStatementFactory.warmUp(lteOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new LTEOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                LTEOperateDefinition lteOperateDefinition = (LTEOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.getOrCreate(lteOperateDefinition.getLeftElement(), helper);
                SqlStatement rightStatement = sqlStatementFactory.getOrCreate(lteOperateDefinition.getRightElement(), helper);
                return new LTEOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
