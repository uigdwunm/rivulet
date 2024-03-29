package zly.rivulet.base.generator.statement;

import zly.rivulet.base.utils.collector.CommonStatementCollector;
import zly.rivulet.base.utils.collector.StatementCollector;

public abstract class AbstractStatement implements Statement {

    protected String cache;

    @Override
    public int getLengthOrCache() {
        if (cache != null) {
            return cache.length();
        } else {
            return this.length();
        }
    }

    @Override
    public void initCache() {
        CommonStatementCollector commonStatementCollector = new CommonStatementCollector();
        this.collectStatementOrCache(commonStatementCollector);
        this.cache = commonStatementCollector.toString();
    }

    @Override
    public void collectStatementOrCache(StatementCollector collector) {
        if (cache != null) {
            collector.append(cache);
        } else {
            this.collectStatement(collector);
        }
    }
}
