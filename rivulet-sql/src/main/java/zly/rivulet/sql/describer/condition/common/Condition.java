package zly.rivulet.sql.describer.condition.common;

import zly.rivulet.sql.describer.condition.ConditionOperate;

import java.util.Arrays;

public interface Condition<F, C> {

    ConditionOperate getOperate();

    @SafeVarargs
    static <F> ConditionContainer<F, ?> and(Condition<F, ?>... items) {
        return new ConditionContainer.AND<>(Arrays.asList(items));
    }

    @SafeVarargs
    static <F> ConditionContainer<F, ?> or(Condition<F, ?>... items) {
        return new ConditionContainer.OR<>(Arrays.asList(items));
    }

    /**
     * =
     */
    BinaryOperation Equal = new BinaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.EQ;
        }
    };

    /**
     * !=
     */
    BinaryOperation NotEqual = new BinaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.NE;
        }
    };

    /**
     * ${value} > ${value}
     */
    BinaryOperation GreaterThan = new BinaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.GT;
        }
    };

    /**
     * ${value} >= ${value}
     */
    BinaryOperation GreaterThanEqual = new BinaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.GTE;
        }
    };

    /**
     * ${value} < ${value}
     */
    BinaryOperation LessThan = new BinaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.LT;
        }
    };

    /**
     * ${value} <= ${value}
     */
    BinaryOperation LessThanEqual = new BinaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.LTE;
        }
    };

    /**
     * ${value} IN (${value})
     */
    BinaryOperation IN = new BinaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.IN;
        }
    };

    /**
     * ${value} NOT IN (${value})
     */
    BinaryOperation NOT_IN = new BinaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.NOT_IN;
        }
    };

    /**
     * ${value} BETWEEN ${value} AND ${value}
     */
    TernaryOperation BETWEEN = new TernaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.BETWEEN;
        }
    };

    /**
     * ${value} NOT NULL
     */
    UnaryOperation NOT_NULL = new UnaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.NOT_NULL;
        }
    };

    /**
     * ${value} IS NULL
     */
    UnaryOperation IS_NULL = new UnaryOperation() {
        @Override
        public ConditionOperate getConditionOperate() {
            return ConditionOperate.IS_NULL;
        }
    };
}
