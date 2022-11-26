package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class EqOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public EqOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
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
        collector.append(Constant.EQ);
        collector.append(rightValue);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            EqOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                EqOperateDefinition eqOperateDefinition = (EqOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.warmUp(eqOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SqlStatement rightStatement = sqlStatementFactory.warmUp(eqOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new EqOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                EqOperateDefinition eqOperateDefinition = (EqOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.getOrCreate(eqOperateDefinition.getLeftElement(), helper);
                SqlStatement rightStatement = sqlStatementFactory.getOrCreate(eqOperateDefinition.getRightElement(), helper);
                return new EqOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
