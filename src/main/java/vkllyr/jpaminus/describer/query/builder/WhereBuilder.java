package vkllyr.jpaminus.describer.query.builder;

import vkllyr.jpaminus.describer.query.desc.WhereOption;

public class WhereBuilder<B> extends GroupBuilder<B> {

    protected WhereBuilder() {}

    public GroupBuilder<B> where(WhereOption<B> ... whereOptions) {
        super.whereOptions = whereOptions;
        return this;
    }
}
