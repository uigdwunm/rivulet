package zly.rivulet.mysql.discriber.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.mysql.discriber.function.date.CurDate;
import zly.rivulet.sql.describer.function.Add;
import zly.rivulet.sql.describer.function.SQLCommonFunction;
import zly.rivulet.sql.describer.function.SQLFunction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public interface MySQLFunction {

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

    interface Date {
        static <F> SQLFunction<F, java.util.Date> curDateToDate() {
            return new SQLCommonFunction<>("CURDATE", null);
        }

        static <F> SQLFunction<F, LocalDate> curDateToLocalDate() {
            return new SQLCommonFunction<>("CURDATE", null);
        }

        static <F> SQLFunction<F, LocalDateTime> curDateToLocalDateTime() {
            return new SQLCommonFunction<>("CURDATE", null);
        }

    }


    interface arithmetical {
        static <F> SQLFunction<F, Integer> powToInt(SingleValueElementDesc<F, Integer> left, SingleValueElementDesc<F, Integer> right) {
            return new SQLCommonFunction<>("POW", Arrays.asList(left, right));
        }
    }

    interface Cast {

    }
}
