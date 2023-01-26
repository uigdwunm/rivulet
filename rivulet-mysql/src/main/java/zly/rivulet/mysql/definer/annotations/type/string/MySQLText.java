package zly.rivulet.mysql.definer.annotations.type.string;

import zly.rivulet.mysql.definer.outerType.StringType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLText {

    class Type implements StringType {
        public Type(MySQLText mySQLInt) {}

        @Override
        public Class<?> getOuterType() {
            return String.class;
        }
    }
}
