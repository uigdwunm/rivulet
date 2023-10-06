package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query_.operate.NotNullOperateDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class NotNullOperateStatement extends OperateStatement {

    private final SingleValueElementStatement value;

    public NotNullOperateStatement(SingleValueElementStatement value) {
        this.value = value;
    }

    @Override
    public int length() {
        return value.singleLength() + Constant.NOT_NULL.length();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        value.singleCollectStatement(collector);
        collector.append(Constant.NOT_NULL);
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            NotNullOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                NotNullOperateDefinition notNullOperateDefinition = (NotNullOperateDefinition) definition;
                SQLStatement valueStatement = sqlStatementFactory.warmUp(notNullOperateDefinition.getValueElementDefinition(), soleFlag.subSwitch(), initHelper);
                return new NotNullOperateStatement((SingleValueElementStatement) valueStatement);
            },
            (definition, helper) -> {
                NotNullOperateDefinition notNullOperateDefinition = (NotNullOperateDefinition) definition;
                SQLStatement valueStatement = sqlStatementFactory.getOrCreate(notNullOperateDefinition.getValueElementDefinition(), helper);
                return new NotNullOperateStatement((SingleValueElementStatement) valueStatement);
            }
        );
    }
}
