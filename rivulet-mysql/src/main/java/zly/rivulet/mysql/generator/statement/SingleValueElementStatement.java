package zly.rivulet.mysql.generator.statement;

import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.generator.statement.SqlStatement;

public interface SingleValueElementStatement extends Statement {

    void singleCollectStatement(StatementCollector collector);

    int singleValueLength();
}
