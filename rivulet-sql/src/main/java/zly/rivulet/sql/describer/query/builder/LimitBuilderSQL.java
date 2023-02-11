package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.query.SQLQueryBuilder;

public class LimitBuilderSQL<F, S> extends SQLQueryBuilder<F, S> {

    public final SQLQueryBuilder<F, S> limit(Param<Integer> limit) {
        super.limit = limit;
        return this;
    }
}
