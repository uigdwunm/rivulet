package vkllyr.jpaminus.describer.query.definition;

import vkllyr.jpaminus.describer.query.definition.conditions.Condition;

import java.util.function.Consumer;

/**
 * Description 查询语句描述，
 *
 * @author zhaolaiyuan
 * Date 2021/9/19 10:48
 **/
public class QueryDesc<B, V> {

    protected final Class<B> queryBase;

    protected final Consumer<V> select;

    protected final Class<V> queryReasult;

    protected final Condition<B, Object>[] whereOptions;

    protected final GroupOption<B>[] groupOptions;

    protected final Condition<B, Object>[] havingOptions;

    protected final OrderOption<V>[] orderOptions;

    protected final LimitOption limitOption;

    public QueryDesc(Class<B> queryBase, Consumer<V> select, Class<V> queryReasult, Condition<B, Object>[] whereOptions, GroupOption<B>[] groupOptions, Condition<B, Object>[] havingOptions, OrderOption<V>[] orderOptions, LimitOption limitOption) {
        this.queryBase = queryBase;
        this.select = select;
        this.queryReasult = queryReasult;
        this.whereOptions = whereOptions;
        this.groupOptions = groupOptions;
        this.havingOptions = havingOptions;
        this.orderOptions = orderOptions;
        this.limitOption = limitOption;
    }
}
