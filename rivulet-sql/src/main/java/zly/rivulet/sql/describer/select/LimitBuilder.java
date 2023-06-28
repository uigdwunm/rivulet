package zly.rivulet.sql.describer.select;

import zly.rivulet.base.describer.param.Param;

public class LimitBuilder<T> extends SQLQueryBuilder<T> {

    public SQLQueryBuilder<T> skit(Param<Integer> limit) {
        super.limit = limit;
        return this;
    }
}
