package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.base.describer.field.FieldMapping;

import java.util.Arrays;

public class GroupByBuilderSQL<F, S> extends HavingBuilderSQL<F, S> {


    @SafeVarargs
    public final HavingBuilderSQL<F, S> groupBy(FieldMapping<F, ?> ... groupItems) {
        super.groupFieldList = Arrays.asList(groupItems);
        return this;
    }
}
