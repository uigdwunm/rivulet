package zly.rivulet.sql.discriber.query;

import zly.rivulet.base.definer.DefinerManager;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.discriber.query.builder.SelectBuilder;
import zly.rivulet.sql.discriber.query.desc.Mapping;
import zly.rivulet.sql.discriber.query.desc.OrderBy;
import zly.rivulet.sql.discriber.query.desc.Where;

import java.util.List;

public class QueryBuilder<F, S> {
    /**
     * 查询主要模型，from
     **/
    protected ModelMeta from;

    /**
     * 查询结果映射模型
     **/
    protected Class<S> selectModel;

    /**
     * 映射的查询列表，
     **/
    protected List<Mapping.Item<F, S, ?>> mappedItemList;

    /**
     * 映射模型剩余字段是否按名称匹配（如果有不匹配会报错）
     **/
    protected boolean nameMapped;

    /**
     * where查询子项
     **/
    protected List<Where.Item<F, ?>> whereItemList;

    /**
     * 分组字段的列表
     **/
    protected List<FieldMapping<F, ?>> groupFieldList;

    /**
     * having查询子项
     **/
    protected List<Where.Item<F, ?>> havingItemList;

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
        ModelMeta modelMeta = DefinerManager.getModelDefinition(from);
        return new SelectBuilder<>(modelMeta, select);
    }

    public final SqlQueryMetaDesc<F, S> build() {
        return new SqlQueryMetaDesc<>(
            this.from,
            this.selectModel,
            this.mappedItemList,
            this.nameMapped,
            this.whereItemList,
            this.groupFieldList,
            this.havingItemList,
            this.orderFieldList,
            this.skit,
            this.limit
        );
    }
}
