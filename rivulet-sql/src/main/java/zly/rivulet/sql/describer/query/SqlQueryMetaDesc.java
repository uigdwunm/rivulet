package zly.rivulet.sql.describer.query;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.custom.SQLPartCustomDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.describer.query.desc.SortItem;

import java.util.List;
import java.util.Map;

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
     * 是否查询返回并映射为单个结果类型
     **/
    protected final boolean isOneResult;

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
    protected final ConditionContainer<?, ?> havingConditionContainer;

    /**
     * order排序子项
     **/
    protected final List<SortItem<F, ?>> orderItemList;

    /**
     * 跳过行数
     **/
    protected final Param<Integer> skit;

    /**
     * 查询行数
     **/
    protected final Param<Integer> limit;

    /**
     * 如果有自定义语句，后面解析的时候会替代原Definition进行解析
     **/
    protected final Map<Class<? extends Definition>, Param<SQLPartCustomDesc>> customStatementMap;

    protected RivuletDesc anno;

    /**
     * TODO 是否包含原生语句
     **/
//    protected final boolean isHaveNativeStatement;

    public SqlQueryMetaDesc(Class<F> modelFrom, Class<S> selectModel, boolean isOneResult, List<Mapping<F, S, ?>> mappedItemList, ConditionContainer<?, ?> whereConditionContainer, List<FieldMapping<F, ?>> groupFieldList, ConditionContainer<?, ?> havingConditionContainer, List<SortItem<F, ?>> orderItemList, Param<Integer> skit, Param<Integer> limit, Map<Class<? extends Definition>, Param<SQLPartCustomDesc>> customStatementMap) {
        this.modelFrom = modelFrom;
        this.selectModel = selectModel;
        this.isOneResult = isOneResult;
        this.mappedItemList = mappedItemList;
        this.whereConditionContainer = whereConditionContainer;
        this.groupFieldList = groupFieldList;
        this.havingConditionContainer = havingConditionContainer;
        this.orderItemList = orderItemList;
        this.skit = skit;
        this.limit = limit;
        this.customStatementMap = customStatementMap;
    }

    public Map<Class<? extends Definition>, Param<SQLPartCustomDesc>> getCustomStatementMap() {
        return customStatementMap;
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

    public ConditionContainer<?, ?> getHavingConditionContainer() {
        return havingConditionContainer;
    }

    public List<SortItem<F, ?>> getOrderItemList() {
        return orderItemList;
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

    @Override
    public Class<?> getReturnType() {
        return selectModel;
    }

    public boolean isOneResult() {
        return isOneResult;
    }
}
