package zly.rivulet.sql.describer.query.condition;

import java.util.List;

public abstract class ConditionContainer<F, C> implements Condition<F, C> {

    private final List<Condition<?, ?>> conditionElementList;

    protected ConditionContainer(List<Condition<?, ?>> conditionElementList) {
        this.conditionElementList = conditionElementList;
    }

    public List<Condition<?, ?>> getConditionElementList() {
        return conditionElementList;
    }

    public static class AND<F, C> extends ConditionContainer<F, C> {

        public AND(List<Condition<?, ?>> conditionElementList) {
            super(conditionElementList);
        }

        @Override
        public ConditionOperate getOperate() {
            return ConditionOperate.AND;
        }
    }

    public static class OR<F, C> extends ConditionContainer<F, C> {

        public OR(List<Condition<?, ?>> conditionElementList) {
            super(conditionElementList);
        }

        @Override
        public ConditionOperate getOperate() {
            return ConditionOperate.OR;
        }
    }
}
