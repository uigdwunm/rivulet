package zly.rivulet.mysql.definer.annotations.type.date;

import zly.rivulet.mysql.definer.outerType.TimeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Date;
import java.sql.Timestamp;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLTimestamp {


    class Type implements TimeType {
        public Type(MySQLTimestamp mySQLDate) {
        }

        @Override
        public Class<?> getOuterType() {
            return Timestamp.class;
        }
    }
}
