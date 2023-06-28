package zly.rivulet.sql.describer.condition.common;

import zly.rivulet.sql.describer.condition.ConditionOperate;

import java.util.List;

public abstract class ConditionContainer implements Condition {

    private final List<Condition> conditionElementList;

    protected ConditionContainer(List<Condition> conditionElementList) {
        this.conditionElementList = conditionElementList;
    }

    public List<Condition> getConditionElementList() {
        return conditionElementList;
    }

    public static class AND extends ConditionContainer {

        public AND(List<Condition> conditionElementList) {
            super(conditionElementList);
        }

        @Override
        public ConditionOperate getOperate() {
            return ConditionOperate.AND;
        }
    }

    public static class OR extends ConditionContainer {

        public OR(List<Condition> conditionElementList) {
            super(conditionElementList);
        }

        @Override
        public ConditionOperate getOperate() {
            return ConditionOperate.OR;
        }
    }
}
