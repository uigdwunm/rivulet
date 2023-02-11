package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;

import java.util.Arrays;

public class WhereByBuilderSQL<F, S> extends GroupByBuilderSQL<F, S> {

    @SafeVarargs
    public final GroupByBuilderSQL<F, S> where(Condition<F, ?>... items) {
        return this.whereAnd(items);
    }

    @SafeVarargs
    public final GroupByBuilderSQL<F, S> whereAnd(Condition<F, ?>... items) {
        super.whereConditionContainer = new ConditionContainer.AND<>(Arrays.asList(items));
        return this;
    }

    @SafeVarargs
    public final GroupByBuilderSQL<F, S> whereOr(Condition<F, ?>... items) {
        super.whereConditionContainer = new ConditionContainer.OR<>(Arrays.asList(items));
        return this;
    }

}
