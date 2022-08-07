package zly.rivulet.mysql.runparser.statement;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.runparser.statement.SqlStatement;

public interface SingleValueElementStatement extends SqlStatement {


    default void singleCollectStatement(StatementCollector collector) {
        collectStatement(collector);
    }
}
