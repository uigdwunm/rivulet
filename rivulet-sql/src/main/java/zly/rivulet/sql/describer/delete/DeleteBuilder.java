package zly.rivulet.sql.describer.delete;

import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.update.SetBuilder;

public class DeleteBuilder<T> {

    /**
     * 要更新的模型
     **/
    protected Class<T> model;

    /**
     * where查询子项
     **/
    protected ConditionContainer<T, ?> whereConditionContainer;

    public static <T> SetBuilder<T> query(Class<T> from) {
        return new SetBuilder<>(from);
    }

    public SQLDeleteMetaDesc<T> build() {
        return new SQLDeleteMetaDesc<>(
            this.model,
            this.whereConditionContainer
        );
    }
}
