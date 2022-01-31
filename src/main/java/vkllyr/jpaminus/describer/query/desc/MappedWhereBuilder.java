package vkllyr.jpaminus.describer.query.desc;

import vkllyr.jpaminus.describer.query.definition.conditions.Condition;

import java.util.function.Consumer;

public class MappedWhereBuilder<B, V> extends MappedGroupBuilder<B, V> {

    public MappedWhereBuilder(Class<B> queryBase, Class<V> queryResult) {
        super.queryBase = queryBase;
        super.queryResult = queryResult;
    }

    public MappedWhereBuilder(Class<B> queryBase, Class<V> queryResult, Consumer<V> select) {
        super.queryBase = queryBase;
        super.queryResult = queryResult;
        super.select = select;
    }

    public MappedGroupBuilder<B, V> where(Condition<B, Object>... whereOptions) {
        super.whereOptions = whereOptions;
        return this;
    }
}
