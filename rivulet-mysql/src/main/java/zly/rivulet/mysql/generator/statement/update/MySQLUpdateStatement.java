package zly.rivulet.mysql.generator.statement.update;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.definer.MySQLModelMeta;
import zly.rivulet.sql.definition.update.SqlUpdateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class MySQLUpdateStatement implements SqlStatement {

    private final SqlUpdateDefinition definition;

    private final MySQLModelMeta mySQLModelMeta;


    public MySQLUpdateStatement(SqlUpdateDefinition definition, MySQLModelMeta mySQLModelMeta, SqlStatement setSQLStatement, SqlStatement whereSQLStatement) {
        this.definition = definition;
        this.mySQLModelMeta = mySQLModelMeta;
    }

    @Override
    public void collectStatement(StatementCollector collector) {

    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SqlUpdateDefinition.class,
            (definition, soleFlag, toolbox) -> {
                SqlUpdateDefinition sqlUpdateDefinition = (SqlUpdateDefinition) definition;
                MySQLModelMeta mySQLModelMeta = (MySQLModelMeta) sqlUpdateDefinition.getFromDefinition().getFrom();
                SqlStatement setSQLStatement = sqlStatementFactory.init(sqlUpdateDefinition.getSetDefinition(), soleFlag.subSwitch(), toolbox);
                SqlStatement whereSQLStatement = sqlStatementFactory.init(sqlUpdateDefinition.getWhereDefinition(), soleFlag.subSwitch(), toolbox);

                return new MySQLUpdateStatement(sqlUpdateDefinition, mySQLModelMeta, setSQLStatement, whereSQLStatement);
            },
            (definition, helper) -> {
                SqlUpdateDefinition sqlUpdateDefinition = (SqlUpdateDefinition) definition;
            }
        );
    }
}
