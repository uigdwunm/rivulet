package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.sql.describer.query.desc.Condition;

import java.util.Arrays;

public class WhereBuilder<F, S> extends GroupBuilder<F, S> {

    @SafeVarargs
    public final GroupBuilder<F, S> where(Condition<F, ?>... items) {
        super.whereItemList = Arrays.asList(items);
        return this;
    }
}
