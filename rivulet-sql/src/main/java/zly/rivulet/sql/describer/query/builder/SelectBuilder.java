package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;

import java.util.Arrays;
import java.util.Collections;

public class SelectBuilder<F, S> extends WhereBuilder<F, S> {

    public SelectBuilder(Class<F> from, Class<S> select) {
        super.modelFrom = from;
        super.selectModel = select;
    }

    public final WhereBuilder<F, S> nameMapped() {
        super.mappedItemList = Collections.emptyList();
        return this;
    }

    @SafeVarargs
    public final WhereBuilder<F, S> select(Mapping.Item<F, S, ?> ... items) {
        super.mappedItemList = Arrays.asList(items);
        return this;
    }
}
