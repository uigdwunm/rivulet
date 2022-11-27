package zly.rivulet.sql.utils.collector;

import zly.rivulet.base.utils.collector.StatementCollector;

public interface SQLStatementCollector extends StatementCollector {

    void collectPlaceholderParam(Object param);
}
