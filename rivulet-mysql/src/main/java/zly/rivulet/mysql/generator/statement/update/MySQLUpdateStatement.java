package zly.rivulet.mysql.generator.statement.update;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.definer.MySQLModelMeta;
import zly.rivulet.mysql.generator.statement.query.WhereStatement;
import zly.rivulet.sql.definition.update.SQLUpdateDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class MySQLUpdateStatement extends SQLStatement {

    private final SQLUpdateDefinition definition;

    private final MySQLModelMeta mySQLModelMeta;

    private final SetStatement setStatement;

    private final WhereStatement whereStatement;

    private static final String UPDATE = "UPDATE ";


    public MySQLUpdateStatement(SQLUpdateDefinition definition, MySQLModelMeta mySQLModelMeta, SetStatement setStatement, WhereStatement whereStatement) {
        this.definition = definition;
        this.mySQLModelMeta = mySQLModelMeta;
        this.setStatement = setStatement;
        this.whereStatement = whereStatement;
    }

    @Override
    public int length() {
        return UPDATE.length() + mySQLModelMeta.getTableName().length() + 1 + setStatement.getLengthOrCache() + whereStatement.getLengthOrCache();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(UPDATE).append(mySQLModelMeta.getTableName()).space();
        collector.append(setStatement);
        collector.space();
        collector.append(whereStatement);
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SQLUpdateDefinition.class,
            (definition, soleFlag, toolbox) -> {
                SQLUpdateDefinition sqlUpdateDefinition = (SQLUpdateDefinition) definition;
                MySQLModelMeta mySQLModelMeta = (MySQLModelMeta) sqlUpdateDefinition.getFromDefinition().getMainFrom();
                SQLStatement setSQLStatement = sqlStatementFactory.warmUp(sqlUpdateDefinition.getSetDefinition(), soleFlag.subSwitch(), toolbox);
                SQLStatement whereSQLStatement = sqlStatementFactory.warmUp(sqlUpdateDefinition.getWhereDefinition(), soleFlag.subSwitch(), toolbox);

                return new MySQLUpdateStatement(sqlUpdateDefinition, mySQLModelMeta, (SetStatement) setSQLStatement, (WhereStatement) whereSQLStatement);
            },
            (definition, toolbox) -> {
                SQLUpdateDefinition sqlUpdateDefinition = (SQLUpdateDefinition) definition;
                MySQLModelMeta mySQLModelMeta = (MySQLModelMeta) sqlUpdateDefinition.getFromDefinition().getMainFrom();
                SQLStatement setSQLStatement = sqlStatementFactory.getOrCreate(sqlUpdateDefinition.getSetDefinition(), toolbox);
                SQLStatement whereSQLStatement = sqlStatementFactory.getOrCreate(sqlUpdateDefinition.getWhereDefinition(), toolbox);

                return new MySQLUpdateStatement(sqlUpdateDefinition, mySQLModelMeta, (SetStatement) setSQLStatement, (WhereStatement) whereSQLStatement);
            }
        );
    }
}
