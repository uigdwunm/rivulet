package vkllyr.jpaminus.describer.query.builder;

import vkllyr.jpaminus.describer.query.desc.*;

public class LimitBuilder<B> extends QueryBuilder<B> {


    protected LimitBuilder() {}

    public QueryBuilder<B> limit(LimitOption limitOption) {
        super.limitOption = limitOption;
        return this;
    }
}
