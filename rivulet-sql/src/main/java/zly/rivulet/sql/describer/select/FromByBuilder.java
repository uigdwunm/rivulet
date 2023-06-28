package zly.rivulet.sql.describer.select;

import zly.rivulet.sql.describer.meta.SQLTableMeta;

public class FromByBuilder<T> extends JoinByBuilder<T> {

    public final JoinByBuilder<T> from(SQLTableMeta sqlTableMeta) {
        super.from = sqlTableMeta;
        return this;
    }
}
