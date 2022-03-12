package zly.rivulet.sql.discriber.query.builder;

import zly.rivulet.base.describer.param.Param;

public class SkitBuilder<F, S> extends LimitBuilder<F, S> {

    public LimitBuilder<F, S> skit(Param<Integer> skit) {
        super.skit = skit;
        return this;
    }
}
