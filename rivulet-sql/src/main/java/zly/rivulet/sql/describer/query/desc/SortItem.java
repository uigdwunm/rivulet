package zly.rivulet.sql.describer.query.desc;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;

public class SortItem<F, C> {

    private final SingleValueElementDesc<F, C> singleValue;

    private final SortType sortType;

    public SortItem(SingleValueElementDesc<F, C> singleValue, SortType sortType) {
        this.singleValue = singleValue;
        this.sortType = sortType;
    }

    public static <F, C> SortItem<F, C> asc(FieldMapping<F, C> fieldMapping) {
        return new SortItem<>(fieldMapping, SortType.ASC);
    }

    public static <F, C> SortItem<F, C> asc(SingleValueElementDesc<F, C> singleValue) {
        return new SortItem<>(singleValue, SortType.ASC);
    }

    public static <F, C> SortItem<F, C> desc(SingleValueElementDesc<F, C> singleValue) {
        return new SortItem<>(singleValue, SortType.DESC);
    }

    public SingleValueElementDesc<F, C> getSingleValue() {
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
