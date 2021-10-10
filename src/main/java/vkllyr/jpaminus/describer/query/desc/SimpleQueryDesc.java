package vkllyr.jpaminus.describer.query.desc;

import java.util.function.Consumer;

public class SimpleQueryDesc<B> extends QueryDesc<B, B> {

    public SimpleQueryDesc(Class<B> queryBase, Consumer<B> select, WhereOption<B>[] whereOptions, GroupOption<B>[] groupOptions, HavingOption<B>[] havingOptions, OrderOption<B>[] orderOptions, LimitOption limitOption) {
        super(queryBase, select, queryBase, whereOptions, groupOptions, havingOptions, orderOptions, limitOption);
    }
}
