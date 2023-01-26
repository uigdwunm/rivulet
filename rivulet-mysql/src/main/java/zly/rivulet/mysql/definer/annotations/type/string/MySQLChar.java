package zly.rivulet.mysql.definer.annotations.type.string;

import zly.rivulet.mysql.definer.outerType.ExactStringType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLChar {

    int length();

    class Type extends ExactStringType {
        public Type(MySQLChar mySQLInt) {
            super(mySQLInt.length());
        }

        @Override
        public Class<?> getOuterType() {
            return String.class;
        }
    }
}
