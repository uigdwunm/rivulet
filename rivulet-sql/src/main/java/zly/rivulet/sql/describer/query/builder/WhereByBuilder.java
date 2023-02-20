package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;

import java.util.Arrays;

public class WhereByBuilder<F, S> extends GroupByBuilder<F, S> {

    @SafeVarargs
    public final GroupByBuilder<F, S> where(Condition<F, ?>... items) {
        return this.whereAnd(items);
    }

    @SafeVarargs
    public final GroupByBuilder<F, S> whereAnd(Condition<F, ?>... items) {
        super.whereConditionContainer = new ConditionContainer.AND<>(Arrays.asList(items));
        return this;
    }

    @SafeVarargs
    public final GroupByBuilder<F, S> whereOr(Condition<F, ?>... items) {
        super.whereConditionContainer = new ConditionContainer.OR<>(Arrays.asList(items));
        return this;
    }

}
