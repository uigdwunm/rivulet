package zly.rivulet.sql.discriber.query.builder;

import zly.rivulet.sql.discriber.query.desc.Where;

public class WhereBuilder<F, S> extends GroupBuilder<F, S> {

    @SafeVarargs
    public final GroupBuilder<F, S> where(Where.Item<F, ?> ... items) {
        return this;
    }
}
