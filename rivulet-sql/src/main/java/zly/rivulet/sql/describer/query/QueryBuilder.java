package zly.rivulet.sql.describer.query;

import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.query.builder.SelectBuilder;
import zly.rivulet.sql.describer.query.condition.Condition;
import zly.rivulet.sql.describer.query.condition.ConditionContainer;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.describer.query.desc.OrderBy;

import java.util.List;

public class QueryBuilder<F, S> {
    /**
     * 查询主要模型，from
     * 有两种赋值方式，
     * modelFrom 是表模型或联表模型
     * subQueryFrom 是子查询模型
     *
     **/
    protected Class<F> modelFrom;

    /**
     * 查询结果映射模型
     **/
    protected Class<S> selectModel;

    /**
     * 映射的查询列表，
     **/
    protected List<Mapping.Item<F, S, ?>> mappedItemList;

    /**
     * where查询子项
     **/
    protected ConditionContainer whereConditionContainer;

    /**
     * 分组字段的列表
     **/
    protected List<FieldMapping<F, ?>> groupFieldList;

    /**
     * having查询子项
     **/
    protected List<Condition<F, ?>> havingItemList;

    /**
     * order排序子项
     **/
    protected List<OrderBy.Item<F, ?>> orderFieldList;

    /**
     * 跳过行数
     **/
    protected Param<Integer> skit;

    /**
     * 查询行数
     **/
    protected Param<Integer> limit;

    public static <F, S> SelectBuilder<F, S> query(Class<F> from, Class<S> select) {
        return new SelectBuilder<>(from, select);
    }

    public final SqlQueryMetaDesc<F, S> build() {
        return new SqlQueryMetaDesc<>(
            this.modelFrom,
            this.selectModel,
            this.mappedItemList,
            this.whereConditionContainer,
            this.groupFieldList,
            this.havingItemList,
            this.orderFieldList,
            this.skit,
            this.limit
        );
    }
}
