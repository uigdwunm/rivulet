package zly.rivulet.mysql.runparser.statement;

import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.mysql.definer.MySQLModelMeta;
import zly.rivulet.sql.runparser.SqlStatementFactory;

public class ModelFromStatement implements QueryFromStatement {

    private final MySQLModelMeta mySQLModelMeta;

    private final String tableName;

    public ModelFromStatement(MySQLModelMeta mySQLModelMeta, String tableName) {
        this.mySQLModelMeta = mySQLModelMeta;
        this.tableName = tableName;
    }

    @Override
    public String createStatement() {
        return tableName;
    }

    @Override
    public void collectStatement(StringBuilder sqlCollector) {
        sqlCollector.append(tableName);
    }

    @Override
    public void formatGetStatement(FormatCollectHelper formatCollectHelper) {
        formatCollectHelper.append(tableName);
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
