package zly.rivulet.mysql.assembly_line.statement;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.assembly_line.statement.SqlStatement;

public interface SingleValueElementStatement extends SqlStatement {


    default void singleCollectStatement(StatementCollector collector) {
        collectStatement(collector);
    }
}
