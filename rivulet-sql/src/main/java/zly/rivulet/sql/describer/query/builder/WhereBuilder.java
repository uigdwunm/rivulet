package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.sql.describer.query.desc.Condition;

public class WhereBuilder<F, S> extends GroupBuilder<F, S> {

    @SafeVarargs
    public final GroupBuilder<F, S> where(Condition<F, F, ?>... items) {
        return this;
    }
}
