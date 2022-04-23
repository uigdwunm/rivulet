package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.base.describer.field.FieldMapping;

import java.util.Arrays;

public class GroupBuilder<F, S> extends HavingBuilder<F, S> {


    @SafeVarargs
    public final HavingBuilder<F, S> groupBy(FieldMapping<F, ?> ... groupItems) {
        super.groupFieldList = Arrays.asList(groupItems);
        return this;
    }
}
