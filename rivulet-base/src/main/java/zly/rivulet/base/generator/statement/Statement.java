package zly.rivulet.base.generator.statement;

import zly.rivulet.base.utils.collector.StatementCollector;

public interface Statement {

    void collectStatement(StatementCollector collector);

}
