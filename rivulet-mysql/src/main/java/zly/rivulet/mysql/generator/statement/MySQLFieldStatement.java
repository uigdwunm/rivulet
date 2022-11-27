package zly.rivulet.mysql.generator.statement;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.definer.MySQLFieldMeta;
import zly.rivulet.sql.generator.SqlStatementFactory;

public class MySQLFieldStatement extends SingleValueElementStatement {

    private final MySQLFieldMeta mySQLFieldMeta;

    public MySQLFieldStatement(MySQLFieldMeta mySQLFieldMeta) {
        this.mySQLFieldMeta = mySQLFieldMeta;
    }

    @Override
    protected int length() {
        return mySQLFieldMeta.getOriginName().length();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(mySQLFieldMeta.getOriginName());
    }

    @Override
    public int singleValueLength() {
        return this.length();
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            MySQLFieldMeta.class,
            (definition, soleFlag, initHelper) -> {
                MySQLFieldMeta mySQLFieldMeta = (MySQLFieldMeta) definition;
                return new MySQLFieldStatement(mySQLFieldMeta);
            },
            (definition, helper) -> {
                MySQLFieldMeta mySQLFieldMeta = (MySQLFieldMeta) definition;
                return new MySQLFieldStatement(mySQLFieldMeta);
            }
        );
    }
}
