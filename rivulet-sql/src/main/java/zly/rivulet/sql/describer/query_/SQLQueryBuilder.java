package zly.rivulet.sql.describer.query_;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.custom.SQLPartCustomDesc;
import zly.rivulet.sql.describer.query_.builder.SelectByBuilder;
import zly.rivulet.sql.describer.query_.desc.Mapping;
import zly.rivulet.sql.describer.select.item.SortItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLQueryBuilder<F, S> {
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
     * 是否查询返回并映射为单个结果类型
     **/
    protected boolean isOneResult = false;

    /**
     * 映射的查询列表，
     **/
    protected List<Mapping<F, S, ?>> mappedItemList;

    /**
     * where查询子项
     **/
    protected ConditionContainer<F, ?> whereConditionContainer;

    /**
     * 分组字段的列表
     **/
    protected List<FieldMapping<F, ?>> groupFieldList;

    /**
     * having查询子项
     **/
    protected ConditionContainer<?, ?> havingConditionContainer;

    /**
     * order排序子项
     **/
    protected List<SortItem<F, ?>> orderItemList;

    /**
     * 跳过行数
     **/
    protected Param<Integer> skit;

    /**
     * 查询行数
     **/
    protected Param<Integer> limit;

    /**
     * 如果有自定义语句，后面解析的时候会替代原Definition进行解析
     **/
    protected Map<Class<? extends Definition>, Param<SQLPartCustomDesc>> customStatementMap = new HashMap<>();

    public static <F, S> SelectByBuilder<F, S> query(Class<F> from, Class<S> select) {
        return new SelectByBuilder<>(from, select);
    }

    public final SQLQueryMetaDesc<S> build() {
        return new SQLQueryMetaDesc<>(
            this.modelFrom,
            this.selectModel,
            this.isOneResult,
            this.mappedItemList,
            this.whereConditionContainer,
            this.groupFieldList,
            this.havingConditionContainer,
            this.orderItemList,
            this.skit,
            this.limit,
            customStatementMap
        );
    }
}
