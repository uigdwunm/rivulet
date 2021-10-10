package vkllyr.jpaminus.describer.query.builder;

import vkllyr.jpaminus.describer.query.desc.OrderOption;

public class OrderBuilder<B> extends LimitBuilder<B> {

    protected OrderBuilder() {}

    public LimitBuilder<B> order(OrderOption<B> ... orderOptions) {
        this.orderOptions = orderOptions;
        return this;
    }
}
