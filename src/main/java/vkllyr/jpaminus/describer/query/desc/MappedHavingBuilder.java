package vkllyr.jpaminus.describer.query.desc;

import vkllyr.jpaminus.describer.query.definition.conditions.Condition;

public class MappedHavingBuilder<B, V> extends MappedOrderBuilder<B, V> {

    public MappedOrderBuilder<B, V> having(Condition<B, Object>... havingOptions) {
        super.havingOptions = havingOptions;
        return this;
    }
}
