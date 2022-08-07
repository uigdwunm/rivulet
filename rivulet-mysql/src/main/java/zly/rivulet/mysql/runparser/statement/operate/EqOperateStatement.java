package zly.rivulet.mysql.runparser.statement.operate;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.runparser.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

public class EqOperateStatement implements OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    private final static String EQ = "=";

    public EqOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        leftValue.singleCollectStatement(collector);
        collector.append(EQ);
        rightValue.singleCollectStatement(collector);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            EqOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                EqOperateDefinition eqOperateDefinition = (EqOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.init(eqOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SqlStatement rightStatement = sqlStatementFactory.init(eqOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
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
