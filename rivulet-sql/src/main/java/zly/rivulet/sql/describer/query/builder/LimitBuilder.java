package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.query.QueryBuilder;

public class LimitBuilder<F, S> extends QueryBuilder<F, S> {

    public final QueryBuilder<F, S> limit(Param<Integer> limit) {
        super.limit = limit;
        return this;
    }
}
