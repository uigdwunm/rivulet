package zly.rivulet.sql.describer.update;

import zly.rivulet.sql.describer.query.desc.Mapping;

import java.util.Arrays;

public class SetBuilder<T> extends WhereBuilder<T> {

    public SetBuilder(Class<T> model) {
        super.model = model;
    }

    @SafeVarargs
    public final WhereBuilder<T> set(Mapping<T, T, ?> ... items) {
        super.mappedItemList = Arrays.asList(items);
        return this;
    }
}
