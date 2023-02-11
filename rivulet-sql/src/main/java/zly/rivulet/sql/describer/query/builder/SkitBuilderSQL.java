package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.base.describer.param.Param;

public class SkitBuilderSQL<F, S> extends LimitBuilderSQL<F, S> {

    public LimitBuilderSQL<F, S> skit(Param<Integer> skit) {
        super.skit = skit;
        return this;
    }
}
