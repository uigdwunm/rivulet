package zly.rivulet.mysql.definer.annotations.type.string;

import zly.rivulet.mysql.definer.outerType.StringType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MySQLText {



    class Type implements StringType {
        public Type(MySQLText mySQLInt) {}

        @Override
        public Class<?> getOuterType() {
            return String.class;
        }
    }
}
