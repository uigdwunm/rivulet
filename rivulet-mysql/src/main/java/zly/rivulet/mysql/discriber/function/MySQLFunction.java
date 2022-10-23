package zly.rivulet.mysql.discriber.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.mysql.discriber.function.cast.Cast;
import zly.rivulet.sql.describer.function.Add;
import zly.rivulet.sql.describer.function.CommonFunction;
import zly.rivulet.sql.describer.function.Function;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public interface MySQLFunction {

    static <F, C> Function<F, C> cast(SingleValueElementDesc<F, C> singleValue, Cast.Type castType) {
        return new Cast<>(singleValue, castType);
    }

    interface Date {
        static <F> Function<F, java.util.Date> curDateToDate() {
            return new CommonFunction<>("CURDATE", null);
        }

        static <F> Function<F, LocalDate> curDateToLocalDate() {
            return new CommonFunction<>("CURDATE", null);
        }

        static <F> Function<F, LocalDateTime> curDateToLocalDateTime() {
            return new CommonFunction<>("CURDATE", null);
        }

    }


    interface Arithmetical {

        @SafeVarargs
        static <F, C> Add<F, C> add(FieldMapping<F, C>... items) {
            return new Add<>(Arrays.asList(items));
        }

        @SafeVarargs
        static <F, C> Add<F, C> add(SingleValueElementDesc<F, C> ... items) {
            return new Add<>(Arrays.asList(items));
        }

        @SafeVarargs
        static <F, C> Add<F, C> add(FieldMapping<F, C> fieldMapping, SingleValueElementDesc<F, C> ... items) {
            ArrayList<SingleValueElementDesc<F, C>> list = new ArrayList<>(items.length + 1);
            list.add(fieldMapping);
            list.addAll(Arrays.asList(items));
            return new Add<>(list);
        }

        static <F> Function<F, Integer> powToInt(SingleValueElementDesc<F, Integer> left, SingleValueElementDesc<F, Integer> right) {
            return new CommonFunction<>("POW", Arrays.asList(left, right));
        }
    }

}
