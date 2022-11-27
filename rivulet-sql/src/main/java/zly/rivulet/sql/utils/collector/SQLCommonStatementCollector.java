package zly.rivulet.sql.utils.collector;

import zly.rivulet.base.utils.collector.CommonStatementCollector;

import java.util.ArrayList;
import java.util.List;

public class SQLCommonStatementCollector extends CommonStatementCollector implements SQLStatementCollector {
    private final List<Object> placeholderParams = new ArrayList<>();

    @Override
    public void collectPlaceholderParam(Object param) {
        placeholderParams.add(param);
    }

    public List<Object> getPlaceholderParams() {
        return placeholderParams;
    }
}
