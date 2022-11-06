package zly.rivulet.sql.describer.delete;

import zly.rivulet.sql.describer.condition.ConditionContainer;
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

    public SqlDeleteMetaDesc<T> build() {
        return new SqlDeleteMetaDesc<>(
            this.model,
            this.whereConditionContainer
        );
    }
}
