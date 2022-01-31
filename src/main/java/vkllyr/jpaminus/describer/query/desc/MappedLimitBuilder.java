package vkllyr.jpaminus.describer.query.desc;

import vkllyr.jpaminus.describer.query.definition.LimitOption;

public class MappedLimitBuilder<B, V> extends MappedQueryBuilder<B, V>{

    public MappedQueryBuilder<B, V> limit(LimitOption limitOption) {
        super.limitOption = limitOption;
        return this;
    }
}
