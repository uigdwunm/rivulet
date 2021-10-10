package vkllyr.jpaminus.describer.query.builder;

import vkllyr.jpaminus.describer.query.desc.conditions.Condition;

public class MappedHavingBuilder<B, V> extends MappedOrderBuilder<B, V> {

    public MappedOrderBuilder<B, V> having(Condition<B, Object>... havingOptions) {
        super.havingOptions = havingOptions;
        return this;
    }
}
