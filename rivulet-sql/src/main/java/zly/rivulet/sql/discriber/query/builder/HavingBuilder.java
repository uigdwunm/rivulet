package zly.rivulet.sql.discriber.query.builder;

import zly.rivulet.sql.discriber.query.desc.Where;

import java.util.Arrays;

public class HavingBuilder<F, S> extends OrderBuilder<F, S> {

    @SafeVarargs
    public final OrderBuilder<F, S> having(Where.Item<F, ?> ... items) {
        super.havingItemList = Arrays.asList(items);
        return this;
    }
}
