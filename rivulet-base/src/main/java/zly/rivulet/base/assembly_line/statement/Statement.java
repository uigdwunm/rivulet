package zly.rivulet.base.assembly_line.statement;

import zly.rivulet.base.utils.collector.StatementCollector;

public interface Statement {

    void collectStatement(StatementCollector collector);

}
