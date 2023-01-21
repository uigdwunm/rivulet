package zly.rivulet.sql.describer.update;

import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.query.desc.Mapping;

import java.util.List;

public class UpdateBuilder<T> {

    /**
     * 要更新的模型
     **/
    protected Class<T> model;

    /**
     * 映射的查询列表，
     **/
    protected List<Mapping<T, T, ?>> mappedItemList;

    /**
     * where查询子项
     **/
    protected ConditionContainer<T, ?> whereConditionContainer;

    public static <T> SetBuilder<T> query(Class<T> from) {
        return new SetBuilder<>(from);
    }

    public SqlUpdateMetaDesc<T> build() {
        return new SqlUpdateMetaDesc<>(
            this.model,
            this.mappedItemList,
            this.whereConditionContainer
        );
    }
}
