package zly.rivulet.sql.describer.select;

import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;

import java.util.Arrays;

public class HivingBuilder<T> extends OrderByBuilder<T> {

    public final OrderByBuilder<T> hiving(Condition... condition) {
        return this.hiving(new ConditionContainer.AND(Arrays.asList(condition)));
    }

    public final OrderByBuilder<T> hiving(ConditionContainer conditionContainer) {
        super.havingConditionContainer = conditionContainer;
        return this;
    }
}
