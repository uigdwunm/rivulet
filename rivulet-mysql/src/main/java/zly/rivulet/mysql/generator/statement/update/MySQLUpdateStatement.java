package zly.rivulet.mysql.generator.statement.update;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.definer.MySQLModelMeta;
import zly.rivulet.mysql.generator.statement.query.WhereStatement;
import zly.rivulet.sql.definition.update.SqlUpdateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class MySQLUpdateStatement implements SqlStatement {

    private final SqlUpdateDefinition definition;

    private final MySQLModelMeta mySQLModelMeta;

    private final SetStatement setStatement;

    private final WhereStatement whereStatement;

    private static final String UPDATE = "UPDATE ";


    public MySQLUpdateStatement(SqlUpdateDefinition definition, MySQLModelMeta mySQLModelMeta, SetStatement setStatement, WhereStatement whereStatement) {
        this.definition = definition;
        this.mySQLModelMeta = mySQLModelMeta;
        this.setStatement = setStatement;
        this.whereStatement = whereStatement;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(UPDATE).append(mySQLModelMeta.getTableName()).space();
        setStatement.collectStatement(collector);
        whereStatement.collectStatement(collector);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SqlUpdateDefinition.class,
            (definition, soleFlag, toolbox) -> {
                SqlUpdateDefinition sqlUpdateDefinition = (SqlUpdateDefinition) definition;
                MySQLModelMeta mySQLModelMeta = (MySQLModelMeta) sqlUpdateDefinition.getFromDefinition().getFrom();
                SqlStatement setSQLStatement = sqlStatementFactory.warmUp(sqlUpdateDefinition.getSetDefinition(), soleFlag.subSwitch(), toolbox);
                SqlStatement whereSQLStatement = sqlStatementFactory.warmUp(sqlUpdateDefinition.getWhereDefinition(), soleFlag.subSwitch(), toolbox);

                return new MySQLUpdateStatement(sqlUpdateDefinition, mySQLModelMeta, (SetStatement) setSQLStatement, (WhereStatement) whereSQLStatement);
            },
            (definition, toolbox) -> {
                SqlUpdateDefinition sqlUpdateDefinition = (SqlUpdateDefinition) definition;
                MySQLModelMeta mySQLModelMeta = (MySQLModelMeta) sqlUpdateDefinition.getFromDefinition().getFrom();
                SqlStatement setSQLStatement = sqlStatementFactory.getOrCreate(sqlUpdateDefinition.getSetDefinition(), toolbox);
                SqlStatement whereSQLStatement = sqlStatementFactory.getOrCreate(sqlUpdateDefinition.getWhereDefinition(), toolbox);

                return new MySQLUpdateStatement(sqlUpdateDefinition, mySQLModelMeta, (SetStatement) setSQLStatement, (WhereStatement) whereSQLStatement);
            }
        );
    }
}
