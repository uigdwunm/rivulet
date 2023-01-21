package zly.rivulet.sql.describer.condition.join;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.describer.condition.ConditionOperate;
import zly.rivulet.sql.describer.condition.common.Condition;

import java.util.Arrays;

public interface JoinCondition<F, C> extends Condition<F, C> {

    static JoinConditionContainer<?, ?> and(JoinCondition<?, ?>... items) {
        return new JoinConditionContainer.AND<>(Arrays.asList(items));
    }

    static JoinConditionContainer<?, ?> or(JoinCondition<?, ?>... items) {
        return new JoinConditionContainer.OR<>(Arrays.asList(items));
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
