package zly.rivulet.base.generator.statement;

import zly.rivulet.base.utils.collector.CommonStatementCollector;
import zly.rivulet.base.utils.collector.StatementCollector;

public abstract class Statement {

    protected String cache;

    public int getLengthOrCache() {
        if (cache != null) {
            return cache.length();
        } else {
            return this.length();
        }
    }

    protected abstract int length();

    public void initCache() {
        CommonStatementCollector commonStatementCollector = new CommonStatementCollector();
        this.collectStatementOrCache(commonStatementCollector);
        this.cache = commonStatementCollector.toString();
    }

    public void collectStatementOrCache(StatementCollector collector) {
        if (cache != null) {
            collector.append(cache);
        } else {
            this.collectStatement(collector);
        }
    }

    public abstract void collectStatement(StatementCollector collector);

}
