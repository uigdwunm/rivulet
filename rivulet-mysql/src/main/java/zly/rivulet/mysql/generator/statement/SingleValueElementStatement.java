package zly.rivulet.mysql.generator.statement;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.generator.statement.SqlStatement;

public abstract class SingleValueElementStatement extends SqlStatement {

    public void singleCollectStatement(StatementCollector collector) {
        collectStatement(collector);
    }

    public abstract int singleValueLength();
}
