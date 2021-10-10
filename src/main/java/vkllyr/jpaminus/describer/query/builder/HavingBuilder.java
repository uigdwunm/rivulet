package vkllyr.jpaminus.describer.query.builder;

import vkllyr.jpaminus.describer.query.desc.HavingOption;

public class HavingBuilder<B> extends OrderBuilder<B> {

    protected HavingBuilder() {}

    public OrderBuilder<B> having(HavingOption<B>... havingOptions) {
        super.havingOptions = havingOptions;
        return this;
    }
}
