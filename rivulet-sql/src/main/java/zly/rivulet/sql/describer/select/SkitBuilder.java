package zly.rivulet.sql.describer.select;

import zly.rivulet.base.describer.param.Param;

public class SkitBuilder<T> extends LimitBuilder<T> {

    public LimitBuilder<T> skit(Param<Integer> skit) {
        super.skit = skit;
        return this;
    }
}
