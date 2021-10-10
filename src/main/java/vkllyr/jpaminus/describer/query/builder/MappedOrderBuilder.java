package vkllyr.jpaminus.describer.query.builder;

import vkllyr.jpaminus.describer.query.desc.OrderOption;

public class MappedOrderBuilder<B, V> extends MappedLimitBuilder<B, V> {

    public MappedLimitBuilder<B, V> order(OrderOption<V>... orderOptions) {
        super.orderOptions = orderOptions;
        return this;
    }
}
