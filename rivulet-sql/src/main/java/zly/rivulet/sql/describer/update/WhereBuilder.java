package zly.rivulet.sql.describer.update;

import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionContainer;

import java.util.Arrays;

public class WhereBuilder<T> extends UpdateBuilder<T> {

    @SafeVarargs
    public final UpdateBuilder<T> where(Condition<T, ?>... items) {
        return this.whereAnd(items);
    }

    @SafeVarargs
    public final UpdateBuilder<T> whereAnd(Condition<T, ?>... items) {
        super.whereConditionContainer = new ConditionContainer.AND<>(Arrays.asList(items));
        return this;
    }

    @SafeVarargs
    public final UpdateBuilder<T> whereOr(Condition<T, ?>... items) {
        super.whereConditionContainer = new ConditionContainer.OR<>(Arrays.asList(items));
        return this;
    }
}
