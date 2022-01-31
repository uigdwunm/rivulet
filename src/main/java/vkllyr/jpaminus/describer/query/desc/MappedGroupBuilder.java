package vkllyr.jpaminus.describer.query.desc;

import vkllyr.jpaminus.describer.query.definition.GroupOption;

public class MappedGroupBuilder<B, V> extends MappedHavingBuilder<B, V> {

    public MappedHavingBuilder<B, V> group(GroupOption<B>... groupOptions) {
        super.groupOptions = groupOptions;
        return this;
    }
}
