package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.sql.describer.query.desc.Mapping;

import java.util.Arrays;

public class SelectByBuilder<F, S> extends WhereByBuilder<F, S> {

    public SelectByBuilder(Class<F> from, Class<S> select) {
        super.modelFrom = from;
        super.selectModel = select;
    }

    @SafeVarargs
    public final WhereByBuilder<F, S> select(Mapping<F, S, ?> ... items) {
        super.mappedItemList = Arrays.asList(items);
        return this;
    }
}
