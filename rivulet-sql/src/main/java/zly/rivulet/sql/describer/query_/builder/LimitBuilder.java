package zly.rivulet.sql.describer.query_.builder;

import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.query_.SQLQueryBuilder;

public class LimitBuilder<F, S> extends SQLQueryBuilder<F, S> {

    public final SQLQueryBuilder<F, S> limit(Param<Integer> limit) {
        super.limit = limit;
        return this;
    }
}
