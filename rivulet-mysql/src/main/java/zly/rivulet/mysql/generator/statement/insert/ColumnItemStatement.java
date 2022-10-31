package zly.rivulet.mysql.generator.statement.insert;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class ColumnItemStatement implements SqlStatement {

    private final SQLFieldMeta sqlFieldMeta;

    public ColumnItemStatement(SQLFieldMeta sqlFieldMeta) {
        this.sqlFieldMeta = sqlFieldMeta;
    }

    public SQLFieldMeta getSqlFieldMeta() {
        return sqlFieldMeta;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(sqlFieldMeta.getOriginName());
    }
}
