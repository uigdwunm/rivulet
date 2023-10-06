package zly.rivulet.sql.describer.select;

import zly.rivulet.sql.describer.meta.SQLQueryMeta;

public class FromByBuilder<T> extends JoinByBuilder<T> {

    public final JoinByBuilder<T> from(SQLQueryMeta SQLQueryMeta) {
        super.from = SQLQueryMeta;
        return this;
    }
}
