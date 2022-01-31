package vkllyr.jpaminus.describer.query.desc;

import java.util.function.Consumer;

public class SelectBuilder<B> extends WhereBuilder<B> {

    protected SelectBuilder(Class<B> queryBase) {
        super.queryBase = queryBase;
    }

    public WhereBuilder<B> select(Consumer<B> select) {
        super.select = select;
        return this;
    }

    public <V> MappedWhereBuilder<B, V> select(Class<V> queryResult) {
        return new MappedWhereBuilder<>(queryBase, queryResult);
    }

    public <V> MappedWhereBuilder<B, V> select(Class<V> queryResult, Consumer<V> select) {
        return new MappedWhereBuilder<>(queryBase, queryResult, select);
    }
}
