package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.sql.describer.query.condition.Condition;
import zly.rivulet.sql.describer.query.condition.ConditionContainer;

import java.util.Arrays;

public class WhereBuilder<F, S> extends GroupBuilder<F, S> {

    @SafeVarargs
    public final GroupBuilder<F, S> where(Condition<F, ?>... items) {
        return this.whereAnd(items);
    }

    @SafeVarargs
    public final GroupBuilder<F, S> whereAnd(Condition<F, ?>... items) {
        super.whereConditionContainer = new ConditionContainer.AND<>(Arrays.asList(items));
        return this;
    }

    @SafeVarargs
    public final GroupBuilder<F, S> whereOr(Condition<F, ?>... items) {
        super.whereConditionContainer = new ConditionContainer.OR<>(Arrays.asList(items));
        return this;
    }

}
