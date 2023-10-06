package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query_.operate.NotLikeOperateDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class NotLikeOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public NotLikeOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public int length() {
        return leftValue.singleLength() + Constant.NOT_LIKE.length() + rightValue.singleLength();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        leftValue.singleCollectStatement(collector);
        collector.append(Constant.NOT_LIKE);
        rightValue.singleCollectStatement(collector);
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            NotLikeOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                NotLikeOperateDefinition notLikeOperateDefinition = (NotLikeOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.warmUp(notLikeOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SQLStatement rightStatement = sqlStatementFactory.warmUp(notLikeOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new NotLikeOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                NotLikeOperateDefinition notLikeOperateDefinition = (NotLikeOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.getOrCreate(notLikeOperateDefinition.getLeftElement(), helper);
                SQLStatement rightStatement = sqlStatementFactory.getOrCreate(notLikeOperateDefinition.getRightElement(), helper);
                return new NotLikeOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
