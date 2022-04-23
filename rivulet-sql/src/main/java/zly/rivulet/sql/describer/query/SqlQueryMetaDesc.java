package zly.rivulet.sql.describer.query;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.query.desc.Condition;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.describer.query.desc.OrderBy;

import java.util.List;

public class SqlQueryMetaDesc<F, S> implements SingleValueElementDesc<F, S>, WholeDesc {
    /**
     * 查询主要模型，from
     * 有两种赋值方式，
     * modelFrom 是表模型或联表模型
     * subQueryFrom 是子查询模型
     *
     **/
    protected final Class<F> modelFrom;

    protected final SqlQueryMetaDesc<?, F> subQueryFrom;

    /**
     * 查询结果映射模型
     **/
    protected final Class<S> selectModel;

    /**
     * 映射的查询列表，
     **/
    protected final List<Mapping.Item<F, S, ?>> mappedItemList;

    /**
     * 映射模型剩余字段是否按名称匹配（如果有不匹配会报错）
     **/
    protected final boolean nameMapped;

    /**
     * where查询子项
     **/
    protected final List<Condition<F, F, ?>> whereItemList;

    /**
     * 分组字段的列表
     **/
    protected final List<FieldMapping<F, ?>> groupFieldList;

    /**
     * having查询子项
     **/
    protected final List<Condition<F, F, ?>> havingItemList;

    /**
     * order排序子项
     **/
    protected final List<OrderBy.Item<F, ?>> orderFieldList;

    /**
     * 跳过行数
     **/
    protected final Param<Integer> skit;

    /**
     * 查询行数
     **/
    protected final Param<Integer> limit;

    public SqlQueryMetaDesc(Class<F> modelFrom, SqlQueryMetaDesc<?, F> subQueryFrom, Class<S> selectModel, List<Mapping.Item<F, S, ?>> mappedItemList, boolean nameMapped, List<Condition<F, F, ?>> whereItemList, List<FieldMapping<F, ?>> groupFieldList, List<Condition<F, F, ?>> havingItemList, List<OrderBy.Item<F, ?>> orderFieldList, Param<Integer> skit, Param<Integer> limit) {
        this.modelFrom = modelFrom;
        this.subQueryFrom = subQueryFrom;
        this.selectModel = selectModel;
        this.mappedItemList = mappedItemList;
        this.nameMapped = nameMapped;
        this.whereItemList = whereItemList;
        this.groupFieldList = groupFieldList;
        this.havingItemList = havingItemList;
        this.orderFieldList = orderFieldList;
        this.skit = skit;
        this.limit = limit;
    }

    public Class<F> getModelFrom() {
        return modelFrom;
    }

    public SqlQueryMetaDesc<?, F> getSubQueryFrom() {
        return subQueryFrom;
    }

    public Class<S> getSelectModel() {
        return selectModel;
    }

    public List<Mapping.Item<F, S, ?>> getMappedItemList() {
        return mappedItemList;
    }

    public boolean isNameMapped() {
        return nameMapped;
    }

    public List<Condition<F, F, ?>> getWhereItemList() {
        return whereItemList;
    }

    public List<FieldMapping<F, ?>> getGroupFieldList() {
        return groupFieldList;
    }

    public List<Condition<F, F, ?>> getHavingItemList() {
        return havingItemList;
    }

    public List<OrderBy.Item<F, ?>> getOrderFieldList() {
        return orderFieldList;
    }

    public Param<Integer> getSkit() {
        return skit;
    }

    public Param<Integer> getLimit() {
        return limit;
    }
}
