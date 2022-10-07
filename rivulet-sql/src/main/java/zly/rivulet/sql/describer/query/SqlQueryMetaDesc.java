package zly.rivulet.sql.describer.query;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.describer.query.desc.OrderBy;

import java.util.List;

public class SqlQueryMetaDesc<F, S> implements SingleValueElementDesc<F, S>, WholeDesc {
    /**
     * 查询主要模型，from
     * 有两种赋值方式，
     * modelFrom 是表模型或联表模型
     *
     **/
    protected final Class<F> modelFrom;

    /**
     * 查询结果映射模型
     **/
    protected final Class<S> selectModel;

    /**
     * 映射的查询列表，
     **/
    protected final List<Mapping<F, S, ?>> mappedItemList;

    /**
     * where查询子项
     **/
    protected final ConditionContainer<?, ?> whereConditionContainer;

    /**
     * 分组字段的列表
     **/
    protected final List<FieldMapping<F, ?>> groupFieldList;

    /**
     * having查询子项
     **/
    protected final List<Condition<F, ?>> havingItemList;

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

    protected RivuletDesc anno;

    /**
     * TODO 是否包含原生语句
     **/
//    protected final boolean isHaveNativeStatement;

    public SqlQueryMetaDesc(Class<F> modelFrom, Class<S> selectModel, List<Mapping<F, S, ?>> mappedItemList, ConditionContainer<?, ?> whereConditionContainer, List<FieldMapping<F, ?>> groupFieldList, List<Condition<F, ?>> havingItemList, List<OrderBy.Item<F, ?>> orderFieldList, Param<Integer> skit, Param<Integer> limit) {
        this.modelFrom = modelFrom;
        this.selectModel = selectModel;
        this.mappedItemList = mappedItemList;
        this.whereConditionContainer = whereConditionContainer;
        this.groupFieldList = groupFieldList;
        this.havingItemList = havingItemList;
        this.orderFieldList = orderFieldList;
        this.skit = skit;
        this.limit = limit;
    }

    public Class<S> getSelectModel() {
        return selectModel;
    }

    public List<Mapping<F, S, ?>> getMappedItemList() {
        return mappedItemList;
    }

    public ConditionContainer<?, ?> getWhereConditionContainer() {
        return whereConditionContainer;
    }

    public List<FieldMapping<F, ?>> getGroupFieldList() {
        return groupFieldList;
    }

    public List<Condition<F, ?>> getHavingItemList() {
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

    @Override
    public Class<?> getMainFrom() {
        return modelFrom;
    }

    @Override
    public RivuletDesc getAnnotation() {
        return this.anno;
    }

    @Override
    public void setAnnotation(RivuletDesc anno) {
        this.anno = anno;
    }

    @Override
    public RivuletFlag getFlag() {
        return RivuletFlag.QUERY;
    }
}
