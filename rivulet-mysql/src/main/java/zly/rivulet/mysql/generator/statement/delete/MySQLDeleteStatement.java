package zly.rivulet.mysql.generator.statement.delete;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.definer.MySQLModelMeta;
import zly.rivulet.mysql.generator.statement.query.WhereStatement;
import zly.rivulet.sql.definition.delete.SqlDeleteDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class MySQLDeleteStatement extends SqlStatement {

    private SqlDeleteDefinition definition;

    private final MySQLModelMeta mySQLModelMeta;

    private final WhereStatement whereStatement;

    private static final String DELETE_FROM = "DELETE FROM ";

    public MySQLDeleteStatement(SqlDeleteDefinition definition, MySQLModelMeta mySQLModelMeta, WhereStatement whereStatement) {
        this.definition = definition;
        this.mySQLModelMeta = mySQLModelMeta;
        this.whereStatement = whereStatement;
    }

    @Override
    protected int length() {
        return DELETE_FROM.length() + mySQLModelMeta.getTableName().length() + 1 + whereStatement.getLengthOrCache();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(DELETE_FROM).append(mySQLModelMeta.getTableName()).space();
        collector.append(whereStatement);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SqlDeleteDefinition.class,
            (definition, soleFlag, toolbox) -> {
                SqlDeleteDefinition sqlDeleteDefinition = (SqlDeleteDefinition) definition;
                MySQLModelMeta mySQLModelMeta = (MySQLModelMeta) sqlDeleteDefinition.getFromDefinition().getMainFrom();
                SqlStatement whereSQLStatement = sqlStatementFactory.warmUp(sqlDeleteDefinition.getWhereDefinition(), soleFlag.subSwitch(), toolbox);

                return new MySQLDeleteStatement(sqlDeleteDefinition, mySQLModelMeta, (WhereStatement) whereSQLStatement);
            },
            (definition, toolbox) -> {
                SqlDeleteDefinition sqlDeleteDefinition = (SqlDeleteDefinition) definition;
                MySQLModelMeta mySQLModelMeta = (MySQLModelMeta) sqlDeleteDefinition.getFromDefinition().getMainFrom();
                SqlStatement whereSQLStatement = sqlStatementFactory.getOrCreate(sqlDeleteDefinition.getWhereDefinition(), toolbox);

                return new MySQLDeleteStatement(sqlDeleteDefinition, mySQLModelMeta, (WhereStatement) whereSQLStatement);
            }
        );
    }
}
