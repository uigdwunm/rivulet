package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.LikeOperateDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class LikeOperateStatement extends OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public LikeOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public int length() {
        return leftValue.singleLength() + Constant.LIKE.length() + rightValue.singleLength();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        leftValue.singleCollectStatement(collector);
        collector.append(Constant.LIKE);
        rightValue.singleCollectStatement(collector);
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            LikeOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                LikeOperateDefinition likeOperateDefinition = (LikeOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.warmUp(likeOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SQLStatement rightStatement = sqlStatementFactory.warmUp(likeOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new LikeOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                LikeOperateDefinition likeOperateDefinition = (LikeOperateDefinition) definition;
                SQLStatement leftStatement = sqlStatementFactory.getOrCreate(likeOperateDefinition.getLeftElement(), helper);
                SQLStatement rightStatement = sqlStatementFactory.getOrCreate(likeOperateDefinition.getRightElement(), helper);
                return new LikeOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
