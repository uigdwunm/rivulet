package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.definition.query.operate.NotNullOperateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class NotNullOperateStatement extends OperateStatement {

    private final SingleValueElementStatement value;

    public NotNullOperateStatement(SingleValueElementStatement value) {
        this.value = value;
    }

    @Override
    protected int length() {
        return value.getLengthOrCache() + Constant.NOT_NULL.length();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(value);
        collector.append(Constant.NOT_NULL);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            NotNullOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                NotNullOperateDefinition notNullOperateDefinition = (NotNullOperateDefinition) definition;
                SqlStatement valueStatement = sqlStatementFactory.warmUp(notNullOperateDefinition.getValueElementDefinition(), soleFlag.subSwitch(), initHelper);
                return new NotNullOperateStatement((SingleValueElementStatement) valueStatement);
            },
            (definition, helper) -> {
                NotNullOperateDefinition notNullOperateDefinition = (NotNullOperateDefinition) definition;
                SqlStatement valueStatement = sqlStatementFactory.getOrCreate(notNullOperateDefinition.getValueElementDefinition(), helper);
                return new NotNullOperateStatement((SingleValueElementStatement) valueStatement);
            }
        );
    }
}