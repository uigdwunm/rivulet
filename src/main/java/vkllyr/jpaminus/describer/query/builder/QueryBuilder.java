package vkllyr.jpaminus.describer.query.builder;

import vkllyr.jpaminus.describer.query.desc.*;

import java.util.function.Consumer;

public class QueryBuilder<B> {
    protected Consumer<B> select;

    protected Class<B> queryBase;

    protected WhereOption<B>[] whereOptions;

    protected GroupOption<B>[] groupOptions;

    protected HavingOption<B>[] havingOptions;

    protected OrderOption<B>[] orderOptions;

    protected LimitOption limitOption;

    protected QueryBuilder() {}


    public static <B> SelectBuilder<B> from(Class<B> queryBase) {
        return new SelectBuilder<>(queryBase);
    }

    public SimpleQueryDesc<B> build() {
        return new SimpleQueryDesc<>(
            this.queryBase,
            this.select,
            this.whereOptions,
            this.groupOptions,
            this.havingOptions,
            this.orderOptions,
            this.limitOption
        );
    }
}
