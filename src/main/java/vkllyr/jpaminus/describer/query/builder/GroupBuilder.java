package vkllyr.jpaminus.describer.query.builder;

import vkllyr.jpaminus.describer.query.desc.GroupOption;

public class GroupBuilder<B> extends HavingBuilder<B> {

    protected GroupBuilder() {}

    public HavingBuilder<B> group(GroupOption<B> ... groupOptions) {
        super.groupOptions = groupOptions;
        return this;
    }
}
