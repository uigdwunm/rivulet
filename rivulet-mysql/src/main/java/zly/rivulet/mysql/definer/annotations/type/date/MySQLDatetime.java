package zly.rivulet.mysql.definer.annotations.type.date;

import zly.rivulet.base.definer.outerType.OriginOuterType;
import zly.rivulet.mysql.definer.outerType.TimeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLDatetime {


    class Type implements TimeType {
        public Type(MySQLDatetime mySQLDate) {
        }

        @Override
        public Class<?> getOuterType() {
            return LocalDateTime.class;
        }
    }
}
