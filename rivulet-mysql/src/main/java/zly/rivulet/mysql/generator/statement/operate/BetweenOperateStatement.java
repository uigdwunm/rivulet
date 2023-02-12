package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.BetweenOperateDefinition;
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
    public int length() {
        return leftValue.singleLength() + Constant.BETWEEN.length() + beforeValue.singleLength() + Constant.AND.length() + afterValue.singleLength();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        leftValue.singleCollectStatement(collector);
        collector.append(Constant.BETWEEN);
        beforeValue.singleCollectStatement(collector);
        collector.append(Constant.AND);
        afterValue.singleCollectStatement(collector);
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
