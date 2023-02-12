package zly.rivulet.base.generator.statement;

import zly.rivulet.base.utils.collector.StatementCollector;

public interface Statement {

    int getLengthOrCache();

    int length();

    void initCache();

    void collectStatementOrCache(StatementCollector collector);

    void collectStatement(StatementCollector collector);

}
