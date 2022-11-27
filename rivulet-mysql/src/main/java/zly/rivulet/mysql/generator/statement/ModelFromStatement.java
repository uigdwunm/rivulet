package zly.rivulet.mysql.generator.statement;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.definer.MySQLModelMeta;
import zly.rivulet.mysql.generator.statement.query.QueryFromStatement;
import zly.rivulet.sql.generator.SqlStatementFactory;

public class ModelFromStatement extends QueryFromStatement {

    private final MySQLModelMeta mySQLModelMeta;

    private final String tableName;

    public ModelFromStatement(MySQLModelMeta mySQLModelMeta, String tableName) {
        this.mySQLModelMeta = mySQLModelMeta;
        this.tableName = tableName;
    }

    @Override
    protected int length() {
        return tableName.length();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(tableName);
    }

    @Override
    public int singleValueLength() {
        return this.length();
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            MySQLModelMeta.class,
            (definition, soleFlag, initHelper) -> {
                MySQLModelMeta mySQLModelMeta = (MySQLModelMeta) definition;
                return new ModelFromStatement(mySQLModelMeta, mySQLModelMeta.getTableName());
            },
            (definition, helper) -> {
                MySQLModelMeta mySQLModelMeta = (MySQLModelMeta) definition;
                return new ModelFromStatement(mySQLModelMeta, mySQLModelMeta.getTableName());
            }
        );
    }
}
