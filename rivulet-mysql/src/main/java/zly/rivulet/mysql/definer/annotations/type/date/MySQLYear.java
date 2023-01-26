package zly.rivulet.mysql.definer.annotations.type.date;

import zly.rivulet.mysql.definer.outerType.TimeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Date;
import java.sql.Time;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLYear {


    class Type implements TimeType {
        public Type(MySQLYear mySQLDate) {
        }

        @Override
        public Class<?> getOuterType() {
            return Date.class;
        }
    }
}
