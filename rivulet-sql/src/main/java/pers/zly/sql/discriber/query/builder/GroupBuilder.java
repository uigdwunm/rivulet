package pers.zly.sql.discriber.query.builder;

import pers.zly.base.describer.field.FieldMapping;

import java.util.Arrays;

public class GroupBuilder<F, S> extends HavingBuilder<F, S> {


    @SafeVarargs
    public final HavingBuilder<F, S> groupBy(FieldMapping<F, ?> ... groupItems) {
        super.groupFieldList = Arrays.asList(groupItems);
        return this;
    }
}
