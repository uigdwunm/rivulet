package zly.rivulet.mysql.generator.statement;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.definer.MySQLModelMeta;
import zly.rivulet.mysql.generator.statement.query.QueryFromStatement;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class ModelFromStatement extends SQLStatement implements QueryFromStatement {

    private final MySQLModelMeta mySQLModelMeta;

    private final String tableName;

    public ModelFromStatement(MySQLModelMeta mySQLModelMeta, String tableName) {
        this.mySQLModelMeta = mySQLModelMeta;
        this.tableName = tableName;
    }

    @Override
    public int length() {
        return tableName.length();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(tableName);
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
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
