package zly.rivulet.mysql.generator.statement.delete;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.definer.MySQLModelMeta;
import zly.rivulet.mysql.generator.statement.query.WhereStatement;
import zly.rivulet.sql.definition.delete.SQLDeleteDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class MySQLDeleteStatement extends SQLStatement {

    private SQLDeleteDefinition definition;

    private final MySQLModelMeta mySQLModelMeta;

    private final WhereStatement whereStatement;

    private static final String DELETE_FROM = "DELETE FROM ";

    public MySQLDeleteStatement(SQLDeleteDefinition definition, MySQLModelMeta mySQLModelMeta, WhereStatement whereStatement) {
        this.definition = definition;
        this.mySQLModelMeta = mySQLModelMeta;
        this.whereStatement = whereStatement;
    }

    @Override
    public int length() {
        return DELETE_FROM.length() + mySQLModelMeta.getTableName().length() + 1 + whereStatement.getLengthOrCache();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(DELETE_FROM).append(mySQLModelMeta.getTableName()).space();
        collector.append(whereStatement);
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SQLDeleteDefinition.class,
            (definition, soleFlag, toolbox) -> {
                SQLDeleteDefinition sqlDeleteDefinition = (SQLDeleteDefinition) definition;
                MySQLModelMeta mySQLModelMeta = (MySQLModelMeta) sqlDeleteDefinition.getFromDefinition().getMainFrom();
                SQLStatement whereSQLStatement = sqlStatementFactory.warmUp(sqlDeleteDefinition.getWhereDefinition(), soleFlag.subSwitch(), toolbox);

                return new MySQLDeleteStatement(sqlDeleteDefinition, mySQLModelMeta, (WhereStatement) whereSQLStatement);
            },
            (definition, toolbox) -> {
                SQLDeleteDefinition sqlDeleteDefinition = (SQLDeleteDefinition) definition;
                MySQLModelMeta mySQLModelMeta = (MySQLModelMeta) sqlDeleteDefinition.getFromDefinition().getMainFrom();
                SQLStatement whereSQLStatement = sqlStatementFactory.getOrCreate(sqlDeleteDefinition.getWhereDefinition(), toolbox);

                return new MySQLDeleteStatement(sqlDeleteDefinition, mySQLModelMeta, (WhereStatement) whereSQLStatement);
            }
        );
    }
}
