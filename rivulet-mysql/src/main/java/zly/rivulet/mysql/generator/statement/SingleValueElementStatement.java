package zly.rivulet.mysql.generator.statement;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.generator.statement.SqlStatement;

public interface SingleValueElementStatement extends SqlStatement {


    default void singleCollectStatement(StatementCollector collector) {
        collectStatement(collector);
    }
}
