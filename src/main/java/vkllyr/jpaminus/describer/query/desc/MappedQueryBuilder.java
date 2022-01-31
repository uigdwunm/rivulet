package vkllyr.jpaminus.describer.query.desc;

import vkllyr.jpaminus.describer.query.definition.GroupOption;
import vkllyr.jpaminus.describer.query.definition.LimitOption;
import vkllyr.jpaminus.describer.query.definition.OrderOption;
import vkllyr.jpaminus.describer.query.definition.QueryDesc;
import vkllyr.jpaminus.describer.query.definition.conditions.Condition;

import java.util.function.Consumer;

public class MappedQueryBuilder<B, V> {

    protected Class<B> queryBase;

    protected Consumer<V> select;

    protected Class<V> queryResult;

    protected Condition<B, Object>[] whereOptions;

    protected GroupOption<B>[] groupOptions;

    protected Condition<B, Object>[] havingOptions;

    protected OrderOption<V>[] orderOptions;

    protected LimitOption limitOption;

    public QueryDesc<B, V> build() {
        return new QueryDesc<>(
            this.queryBase,
            this.select,
            this.queryResult,
            this.whereOptions,
            this.groupOptions,
            this.havingOptions,
            this.orderOptions,
            this.limitOption
        );
    }
}
