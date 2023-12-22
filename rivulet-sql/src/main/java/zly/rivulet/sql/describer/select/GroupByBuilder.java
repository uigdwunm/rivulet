package zly.rivulet.sql.describer.select;

import zly.rivulet.sql.definer.meta.SQLColumnMeta;

import java.util.Arrays;

public class GroupByBuilder<T> extends HivingBuilder<T> {

    public final HivingBuilder<T> groupBy(SQLColumnMeta ... sqlColumns) {
        super.groupColumnList = Arrays.asList(sqlColumns);
        return this;
    }
}
