package vkllyr.jpaminus.describer.query.desc.conditions;

import vkllyr.jpaminus.describer.query.desc.Param;
import vkllyr.jpaminus.parser.definition.sqlPart.CheckCondition;
import vkllyr.jpaminus.describer.query.model.SqlPart;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class Condition<B, P> {

    private final CheckCondition<P> checkCondition;

    private final Function<B, P> field;

    private final SqlPart sqlPart;

    public Condition(CheckCondition<P> checkCondition, Function<B, P> field, SqlPart sqlPart) {
        this.checkCondition = checkCondition;
        this.field = field;
        this.sqlPart = sqlPart;
    }

    public static <B, P> Condition<B, P> between(Function<B, P> field, Param<P> leftParam, Param<P> rightParam, BiPredicate<P, P> checkParam) {
        CheckCondition<P> checkCondition = new CheckCondition<P>(leftParam, rightParam) {
            @Override
            public boolean check(Object[] params) {
                return checkParam.test(params[0], params[1]);
            }
        };

        SqlPart sqlPart = new SqlPart(
            new String[]{null, " BETWEEN ", null, " AND ", null},
            0,
            new int[]{2, 4}
        );

        return new Condition<>(checkCondition, field, sqlPart);
    }

    public static <B, P> Condition<B, P> between(Function<B, P> field, Param<P> leftParam, Param<P> rightParam) {
        return between(field, leftParam, rightParam, (l, r) -> true);
    }

    public static <B, P> Condition<B, P> isNull(Function<B, P> field, Param<P> param, Predicate<P> checkParam) {
        CheckCondition<P> checkCondition = new CheckCondition<P>(param) {
            @Override
            public boolean check(Object[] params) {
                return checkParam.test((P) params[0]);
            }
        };

        SqlPart sqlPart = new SqlPart(
            new String[]{null, " IS NULL "},
            0,
            new int[]{}
        );

        return new Condition<>(checkCondition, field, sqlPart);
    }

    public static <B, P> Condition<B, P> equal(Function<B, P> fieldA, Function<B, P> fieldB, Param<P> param, Predicate<P> checkParam) {
        CheckCondition<P> checkCondition = new CheckCondition<P>(param) {
            @Override
            public boolean check(Object[] params) {
                return checkParam.test((P) params[0]);
            }
        };

        SqlPart sqlPart = new SqlPart(
            new String[]{null, "=", null},
            0,
            new int[]{}
        );

        return new Condition<>(checkCondition, field, sqlPart);
    }
}
