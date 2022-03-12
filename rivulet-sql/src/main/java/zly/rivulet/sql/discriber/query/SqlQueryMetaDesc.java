package zly.rivulet.sql.discriber.query;

import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.describer.Desc;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.discriber.query.desc.Mapping;
import zly.rivulet.sql.discriber.query.desc.OrderBy;
import zly.rivulet.sql.discriber.query.desc.Where;

import java.util.List;

public class SqlQueryMetaDesc<F, S> implements SingleValueElementDesc<F, S>, Desc {
    /**
     * 查询主要模型，from
     **/
    protected final ModelMeta from;

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
    protected final List<Where.Item<F, ?>> whereItemList;

    /**
     * 分组字段的列表
     **/
    protected final List<FieldMapping<F, ?>> groupFieldList;

    /**
     * having查询子项
     **/
    protected final List<Where.Item<F, ?>> havingItemList;

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

    public SqlQueryMetaDesc(ModelMeta from, Class<S> selectModel, List<Mapping.Item<F, S, ?>> mappedItemList, boolean nameMapped, List<Where.Item<F, ?>> whereItemList, List<FieldMapping<F, ?>> groupFieldList, List<Where.Item<F, ?>> havingItemList, List<OrderBy.Item<F, ?>> orderFieldList, Param<Integer> skit, Param<Integer> limit) {
        this.from = from;
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

    public ModelMeta getFrom() {
        return from;
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

    public List<Where.Item<F, ?>> getWhereItemList() {
        return whereItemList;
    }

    public List<FieldMapping<F, ?>> getGroupFieldList() {
        return groupFieldList;
    }

    public List<Where.Item<F, ?>> getHavingItemList() {
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
