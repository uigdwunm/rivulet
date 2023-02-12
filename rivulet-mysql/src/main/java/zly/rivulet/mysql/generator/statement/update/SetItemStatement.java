package zly.rivulet.mysql.generator.statement.update;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.mysql.generator.statement.query.MapStatement;
import zly.rivulet.sql.definition.update.SetItemDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class SetItemStatement extends SqlStatement {

    private final SingleValueElementStatement singleValueElementStatement;

    private final MapStatement mapStatement;

    public SetItemStatement(SingleValueElementStatement singleValueElementStatement, MapStatement mapStatement) {
        this.singleValueElementStatement = singleValueElementStatement;
        this.mapStatement = mapStatement;
    }


    @Override
    public int length() {
        return mapStatement.getLengthOrCache() + 1 + singleValueElementStatement.getLengthOrCache();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(mapStatement);
        collector.append(Constant.EQ);
        collector.append(singleValueElementStatement);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SetItemDefinition.class,
            (definition, soleFlag, toolbox) -> {
                SetItemDefinition setItemDefinition = (SetItemDefinition) definition;
                SqlStatement singleValueStatement = sqlStatementFactory.warmUp(setItemDefinition.getValueDefinition(), soleFlag.subSwitch(), toolbox);
                SqlStatement fieldStatement = sqlStatementFactory.warmUp(setItemDefinition.getFieldMap(), soleFlag.subSwitch(), toolbox);
                return new SetItemStatement((SingleValueElementStatement) singleValueStatement, (MapStatement) fieldStatement);
            },
            (definition, toolbox) -> {
                SetItemDefinition setItemDefinition = (SetItemDefinition) definition;
                SqlStatement singleValueStatement = sqlStatementFactory.getOrCreate(setItemDefinition.getValueDefinition(), toolbox);
                SqlStatement fieldStatement = sqlStatementFactory.getOrCreate(setItemDefinition.getFieldMap(), toolbox);
                return new SetItemStatement((SingleValueElementStatement) singleValueStatement, (MapStatement) fieldStatement);
            }
        );
    }
}
