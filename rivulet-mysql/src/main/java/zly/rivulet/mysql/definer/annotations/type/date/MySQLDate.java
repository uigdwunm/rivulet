package zly.rivulet.mysql.definer.annotations.type.date;

import zly.rivulet.mysql.definer.outerType.TimeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Date;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLDate {


    class Type implements TimeType {
        public Type(MySQLDate mySQLDate) {
        }

        @Override
        public Class<?> getOuterType() {
            // java.sql.Date
            return Date.class;
        }
    }
}
