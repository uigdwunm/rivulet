package zly.rivulet.sql.discriber.query.builder;

import zly.rivulet.sql.discriber.query.desc.OrderBy;

import java.util.Arrays;

public class OrderBuilder<F, S> extends SkitBuilder<F, S> {

    @SafeVarargs
    public final SkitBuilder<F, S> orderBy(OrderBy.Item<F, ?> ... items) {
        super.orderFieldList = Arrays.asList(items);
        return this;
    }
}
