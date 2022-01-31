package vkllyr.jpaminus.describer.query.desc;

import vkllyr.jpaminus.describer.query.definition.HavingOption;

public class HavingBuilder<B> extends OrderBuilder<B> {

    protected HavingBuilder() {}

    public OrderBuilder<B> having(HavingOption<B>... havingOptions) {
        super.havingOptions = havingOptions;
        return this;
    }
}
