package zly.rivulet.sql.describer.query_;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.definer.meta.SQLColumnMeta;
import zly.rivulet.sql.definer.meta.SQLQueryMeta;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.custom.SQLPartCustomDesc;
import zly.rivulet.sql.describer.select.item.CommonMapping;
import zly.rivulet.sql.describer.select.item.JoinItem;
import zly.rivulet.sql.describer.select.item.Mapping;
import zly.rivulet.sql.describer.select.item.SortItem;

import java.util.List;
import java.util.Map;

public class SQLQueryMetaDesc<T> implements SingleValueElementDesc<T>, WholeDesc {

    /**
     * 查询结果映射模型
     **/
    private final Class<T> resultModelClass;

    /**
     * 是否查询返回并映射为单个结果类型
     **/
    private final boolean isOneResult;

    /**
     * 映射的查询列表，
     **/
    private final List<Mapping<T>> mappedItemList;

    private SQLQueryMeta from;

    private List<JoinItem> joinList;

    /**
     * where查询子项
     **/
    private final ConditionContainer whereConditionContainer;

    /**
     * 分组字段的列表
     **/
    private final List<SQLColumnMeta<?>> groupColumnList;

    /**
     * having查询子项
     **/
    private final ConditionContainer havingConditionContainer;

    /**
     * order排序子项
     **/
    private final List<SortItem> orderItemList;

    /**
     * 跳过行数
     **/
    private final Param<Integer> skit;

    /**
     * 查询行数
     **/
    private final Param<Integer> limit;

    /**
     * 如果有自定义语句，后面解析的时候会替代原Definition进行解析
     **/
    private final Map<Class<? extends Definition>, Param<SQLPartCustomDesc>> customStatementMap;

    private RivuletDesc anno;

    public SQLQueryMetaDesc(
            Class<T> resultModelClass,
            boolean isOneResult,
            List<Mapping<T>> mappedItemList,
            SQLQueryMeta from,
            List<JoinItem> joinList,
            ConditionContainer whereConditionContainer,
            List<SQLColumnMeta<?>> groupColumnList,
            ConditionContainer havingConditionContainer,
            List<SortItem> orderItemList,
            Param<Integer> skit,
            Param<Integer> limit,
            Map<Class<? extends Definition>, Param<SQLPartCustomDesc>> customStatementMap
    ) {
        this.resultModelClass = resultModelClass;
        this.isOneResult = isOneResult;
        this.mappedItemList = mappedItemList;
        this.from = from;
        this.joinList = joinList;
        this.whereConditionContainer = whereConditionContainer;
        this.groupColumnList = groupColumnList;
        this.havingConditionContainer = havingConditionContainer;
        this.orderItemList = orderItemList;
        this.skit = skit;
        this.limit = limit;
        this.customStatementMap = customStatementMap;
    }

    public Class<T> getResultModelClass() {
        return resultModelClass;
    }

    public boolean isOneResult() {
        return isOneResult;
    }

    public List<Mapping<T>> getMappedItemList() {
        return mappedItemList;
    }

    public SQLQueryMeta getFrom() {
        return from;
    }

    public List<JoinItem> getJoinList() {
        return joinList;
    }

    public ConditionContainer getWhereConditionContainer() {
        return whereConditionContainer;
    }

    public List<SQLColumnMeta<?>> getGroupColumnList() {
        return groupColumnList;
    }

    public ConditionContainer getHavingConditionContainer() {
        return havingConditionContainer;
    }

    public List<SortItem> getOrderItemList() {
        return orderItemList;
    }

    public Param<Integer> getSkit() {
        return skit;
    }

    public Param<Integer> getLimit() {
        return limit;
    }

    public Map<Class<? extends Definition>, Param<SQLPartCustomDesc>> getCustomStatementMap() {
        return customStatementMap;
    }

    @Override
    public Class<?> getReturnType() {
        return resultModelClass;
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
    public Class<T> getTargetType() {
        return this.resultModelClass;
    }
}
