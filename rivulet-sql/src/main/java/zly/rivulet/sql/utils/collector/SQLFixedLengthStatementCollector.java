package zly.rivulet.sql.utils.collector;

import zly.rivulet.base.utils.collector.FixedLengthStatementCollector;

import java.util.ArrayList;
import java.util.List;

public class SQLFixedLengthStatementCollector extends FixedLengthStatementCollector implements SQLStatementCollector {

    private final List<Object> placeholderParams = new ArrayList<>();

    public SQLFixedLengthStatementCollector(int length) {
        super(length);
    }

    @Override
    public void collectPlaceholderParam(Object param) {
        placeholderParams.add(param);
    }

    public List<Object> getPlaceholderParams() {
        return placeholderParams;
    }
}
