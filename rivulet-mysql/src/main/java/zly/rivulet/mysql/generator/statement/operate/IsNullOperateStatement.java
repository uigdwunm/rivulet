package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.IsNullOperateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class IsNullOperateStatement extends OperateStatement {

    private final SingleValueElementStatement value;

    public IsNullOperateStatement(SingleValueElementStatement value) {
        this.value = value;
    }

    @Override
    public int length() {
        return value.singleLength() + Constant.IS_NULL.length();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        value.singleCollectStatement(collector);
        collector.append(Constant.IS_NULL);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            IsNullOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                IsNullOperateDefinition isNullOperateDefinition = (IsNullOperateDefinition) definition;
                SqlStatement valueStatement = sqlStatementFactory.warmUp(isNullOperateDefinition.getValueElementDefinition(), soleFlag.subSwitch(), initHelper);
                return new IsNullOperateStatement((SingleValueElementStatement) valueStatement);
            },
            (definition, helper) -> {
                IsNullOperateDefinition isNullOperateDefinition = (IsNullOperateDefinition) definition;
                SqlStatement valueStatement = sqlStatementFactory.getOrCreate(isNullOperateDefinition.getValueElementDefinition(), helper);
                return new IsNullOperateStatement((SingleValueElementStatement) valueStatement);
            }
        );
    }
}
