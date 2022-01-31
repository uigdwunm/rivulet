package vkllyr.jpaminus.describer.query.desc;

import vkllyr.jpaminus.describer.query.definition.WhereOption;

public class WhereBuilder<B> extends GroupBuilder<B> {

    protected WhereBuilder() {}

    public GroupBuilder<B> where(WhereOption<B> ... whereOptions) {
        super.whereOptions = whereOptions;
        return this;
    }
}
