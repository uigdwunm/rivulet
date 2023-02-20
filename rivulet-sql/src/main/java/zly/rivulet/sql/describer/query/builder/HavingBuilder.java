package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;

import java.util.Arrays;

public class HavingBuilder<F, S> extends OrderByBuilder<F, S> {

    @SafeVarargs
    public final OrderByBuilder<F, S> having(Condition<F, ?>... items) {
        return this.havingAnd(items);
    }

    @SafeVarargs
    public final OrderByBuilder<F, S> havingAnd(Condition<F, ?>... items) {
        super.whereConditionContainer = new ConditionContainer.AND<>(Arrays.asList(items));
        return this;
    }

    @SafeVarargs
    public final OrderByBuilder<F, S> havingOr(Condition<F, ?>... items) {
        super.whereConditionContainer = new ConditionContainer.OR<>(Arrays.asList(items));
        return this;
    }
}
