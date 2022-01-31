package vkllyr.jpaminus.describer.query.desc;

import vkllyr.jpaminus.describer.query.definition.OrderOption;

public class MappedOrderBuilder<B, V> extends MappedLimitBuilder<B, V> {

    public MappedLimitBuilder<B, V> order(OrderOption<V>... orderOptions) {
        super.orderOptions = orderOptions;
        return this;
    }
}
