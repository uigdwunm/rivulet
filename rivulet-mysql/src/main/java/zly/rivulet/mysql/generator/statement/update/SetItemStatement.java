package zly.rivulet.mysql.generator.statement.update;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.FieldStatement;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.update.SetItemDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class SetItemStatement implements SqlStatement {

    private final SingleValueElementStatement singleValueElementStatement;

    private final FieldStatement fieldStatement;

    public SetItemStatement(SingleValueElementStatement singleValueElementStatement, FieldStatement fieldStatement) {
        this.singleValueElementStatement = singleValueElementStatement;
        this.fieldStatement = fieldStatement;
    }


    @Override
    public void collectStatement(StatementCollector collector) {
        fieldStatement.collectStatement(collector);
        collector.append(Constant.EQ);
        singleValueElementStatement.collectStatement(collector);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SetItemDefinition.class,
            (definition, soleFlag, toolbox) -> {
                SetItemDefinition setItemDefinition = (SetItemDefinition) definition;
                SqlStatement singleValueStatement = sqlStatementFactory.init(setItemDefinition.getValueDefinition(), soleFlag.subSwitch(), toolbox);
                SqlStatement fieldStatement = sqlStatementFactory.init(setItemDefinition.getFieldDefinition(), soleFlag.subSwitch(), toolbox);
                return new SetItemStatement((SingleValueElementStatement) singleValueStatement, (FieldStatement) fieldStatement);
            },
            (definition, toolbox) -> {
                SetItemDefinition setItemDefinition = (SetItemDefinition) definition;
                SqlStatement singleValueStatement = sqlStatementFactory.getOrCreate(setItemDefinition.getValueDefinition(), toolbox);
                SqlStatement fieldStatement = sqlStatementFactory.getOrCreate(setItemDefinition.getFieldDefinition(), toolbox);
                return new SetItemStatement((SingleValueElementStatement) singleValueStatement, (FieldStatement) fieldStatement);
            }
        );
    }
}
