package pers.zly.sql.discriber.query.builder;

import pers.zly.base.describer.param.Param;
import pers.zly.sql.discriber.query.QueryBuilder;

public class LimitBuilder<F, S> extends QueryBuilder<F, S> {

    public final QueryBuilder<F, S> limit(Param<Integer> limit) {
        super.limit = limit;
        return this;
    }
}
