package vkllyr.jpaminus.describer.query.builder;

import vkllyr.jpaminus.describer.query.desc.LimitOption;

public class MappedLimitBuilder<B, V> extends MappedQueryBuilder<B, V>{

    public MappedQueryBuilder<B, V> limit(LimitOption limitOption) {
        super.limitOption = limitOption;
        return this;
    }
}
