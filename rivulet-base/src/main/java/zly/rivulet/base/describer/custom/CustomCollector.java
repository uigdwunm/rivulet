package zly.rivulet.base.describer.custom;

import zly.rivulet.base.utils.collector.StatementCollector;

import java.util.List;

public class CustomCollector {

    private final StatementCollector collector;

    public CustomCollector(StatementCollector collector) {
        this.collector = collector;
    }

    public CustomCollector space() {
        collector.space();
        return this;
    }

    public CustomCollector append(CustomSingleValueWrap singleValueWrap) {
        singleValueWrap.collectSingleValue(collector);
        return this;
    }

    public CustomCollector append(String statementPart) {
        collector.append(statementPart);
        return this;
    }

    public CustomCollector appendAllSeparator(List<CustomSingleValueWrap> singleValueWraps, String separator) {
        for (CustomSingleValueWrap singleValueWrap : collector.createJoiner(separator, singleValueWraps)) {
            singleValueWrap.collectSingleValue(collector);
        }
        return this;
    }
}
