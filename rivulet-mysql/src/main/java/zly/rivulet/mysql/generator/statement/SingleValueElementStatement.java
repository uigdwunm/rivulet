package zly.rivulet.mysql.generator.statement;

import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.utils.collector.StatementCollector;

/**
 * 单值类型，单独弄个接口主要是有的带括号有的不带
 **/
public interface SingleValueElementStatement extends Statement {

    default int singleLength() {
        return this.getLengthOrCache();
    }

    default void singleCollectStatement(StatementCollector collector) {
        this.collectStatementOrCache(collector);
    }
}
