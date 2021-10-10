package vkllyr.jpaminus.describer.query.builder;

import vkllyr.jpaminus.describer.query.desc.GroupOption;

public class MappedGroupBuilder<B, V> extends MappedHavingBuilder<B, V> {

    public MappedHavingBuilder<B, V> group(GroupOption<B>... groupOptions) {
        super.groupOptions = groupOptions;
        return this;
    }
}
