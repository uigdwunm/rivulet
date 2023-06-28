package zly.rivulet.sql.describer.select.item;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;

public class SortItem {

    private final SingleValueElementDesc singleValue;

    private final SortType sortType;

    public SortItem(SingleValueElementDesc singleValue, SortType sortType) {
        this.singleValue = singleValue;
        this.sortType = sortType;
    }

    public static SortItem asc(FieldMapping fieldMapping) {
        return new SortItem(fieldMapping, SortType.ASC);
    }

    public static SortItem asc(SingleValueElementDesc singleValue) {
        return new SortItem(singleValue, SortType.ASC);
    }

    public static SortItem desc(SingleValueElementDesc singleValue) {
        return new SortItem(singleValue, SortType.DESC);
    }

    public SingleValueElementDesc getSingleValue() {
        return singleValue;
    }

    public SortType getSortType() {
        return sortType;
    }

    public static enum SortType {
        ASC,
        DESC;
    }
}
