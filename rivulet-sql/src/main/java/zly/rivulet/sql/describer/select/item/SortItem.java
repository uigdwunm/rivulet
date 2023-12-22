package zly.rivulet.sql.describer.select.item;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.definer.meta.SQLColumnMeta;

public class SortItem {

    private final SingleValueElementDesc<?> singleValue;

    private final SortType sortType;

    public SortItem(SingleValueElementDesc<?> singleValue, SortType sortType) {
        this.singleValue = singleValue;
        this.sortType = sortType;
    }

    public static SortItem asc(SQLColumnMeta<?> sqlColumnMeta) {
        return new SortItem(sqlColumnMeta, SortType.ASC);
    }

    public static SortItem asc(SQLFunction<?> sqlFunction) {
        return new SortItem(sqlFunction, SortType.ASC);
    }

    public static SortItem desc(SQLColumnMeta<?> sqlColumnMeta) {
        return new SortItem(sqlColumnMeta, SortType.DESC);
    }

    public static SortItem desc(SQLFunction<?> sqlFunction) {
        return new SortItem(sqlFunction, SortType.DESC);
    }

    public SingleValueElementDesc<?> getSingleValue() {
        return singleValue;
    }

    public SortType getSortType() {
        return sortType;
    }

    public enum SortType {
        ASC,
        DESC;
    }
}
