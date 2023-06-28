package zly.rivulet.sql.describer.select;

import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;

import java.util.Arrays;

public class WhereByBuilder<T> extends GroupByBuilder<T> {

    public final GroupByBuilder<T> where(Condition... condition) {
        return this.where(new ConditionContainer.AND(Arrays.asList(condition)));
    }

    public final GroupByBuilder<T> where(ConditionContainer conditionContainer) {
        super.whereConditionContainer = conditionContainer;
        return this;
    }
}
