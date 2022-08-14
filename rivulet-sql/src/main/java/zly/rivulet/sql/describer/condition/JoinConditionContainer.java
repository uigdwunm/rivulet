package zly.rivulet.sql.describer.condition;

import java.util.List;

public abstract class JoinConditionContainer<F, C> implements JoinCondition<F, C> {

    private final List<JoinCondition<?, ?>> conditionElementList;

    protected JoinConditionContainer(List<JoinCondition<?, ?>> conditionElementList) {
        this.conditionElementList = conditionElementList;
    }

    public List<JoinCondition<?, ?>> getConditionElementList() {
        return conditionElementList;
    }

    public static class AND<F, C> extends JoinConditionContainer<F, C> {

        public AND(List<JoinCondition<?, ?>> conditionElementList) {
            super(conditionElementList);
        }

        @Override
        public ConditionOperate getOperate() {
            return ConditionOperate.AND;
        }
    }

    public static class OR<F, C> extends JoinConditionContainer<F, C> {

        public OR(List<JoinCondition<?, ?>> conditionElementList) {
            super(conditionElementList);
        }

        @Override
        public ConditionOperate getOperate() {
            return ConditionOperate.OR;
        }
    }
}
