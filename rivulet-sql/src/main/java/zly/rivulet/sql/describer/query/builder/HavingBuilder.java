package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.sql.describer.condition.Condition;

import java.util.Arrays;

public class HavingBuilder<F, S> extends OrderByBuilder<F, S> {

    @SafeVarargs
    public final OrderByBuilder<F, S> having(Condition<F, ?>... items) {
        super.havingItemList = Arrays.asList(items);
        return this;
    }
}
