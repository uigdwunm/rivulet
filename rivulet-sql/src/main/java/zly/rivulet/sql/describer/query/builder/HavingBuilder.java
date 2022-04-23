package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.sql.describer.query.desc.Condition;

import java.util.Arrays;

public class HavingBuilder<F, S> extends OrderBuilder<F, S> {

    @SafeVarargs
    public final OrderBuilder<F, S> having(Condition<F, ?>... items) {
        super.havingItemList = Arrays.asList(items);
        return this;
    }
}
