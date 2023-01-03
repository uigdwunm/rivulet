package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.BetweenOperateDefinition;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class BetweenOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement beforeValue;

    private final SingleValueElementStatement afterValue;

    public BetweenOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement beforeValue, SingleValueElementStatement afterValue) {
        this.leftValue = leftValue;
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
    }

    @Override
    protected int length() {
        return leftValue.getLengthOrCache() + Constant.BETWEEN.length() + beforeValue.getLengthOrCache() + Constant.AND.length() + afterValue.getLengthOrCache();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(leftValue);
        collector.append(Constant.BETWEEN);
        collector.append(beforeValue);
        collector.append(Constant.AND);
        collector.append(afterValue);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            BetweenOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                BetweenOperateDefinition betweenOperateDefinition = (BetweenOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.warmUp(betweenOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SqlStatement beforeStatement = sqlStatementFactory.warmUp(betweenOperateDefinition.getBeforeElement(), soleFlag.subSwitch(), initHelper);
                SqlStatement afterStatement = sqlStatementFactory.warmUp(betweenOperateDefinition.getAfterElement(), soleFlag.subSwitch(), initHelper);
                return new BetweenOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) beforeStatement, (SingleValueElementStatement) afterStatement);
            },
            (definition, helper) -> {
                BetweenOperateDefinition betweenOperateDefinition = (BetweenOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.getOrCreate(betweenOperateDefinition.getLeftElement(), helper);
                SqlStatement beforeStatement = sqlStatementFactory.getOrCreate(betweenOperateDefinition.getBeforeElement(), helper);
                SqlStatement afterStatement = sqlStatementFactory.getOrCreate(betweenOperateDefinition.getAfterElement(), helper);
                return new BetweenOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) beforeStatement, (SingleValueElementStatement) afterStatement);
            }
        );
    }
}
