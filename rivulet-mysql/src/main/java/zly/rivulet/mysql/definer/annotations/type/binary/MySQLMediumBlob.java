package zly.rivulet.mysql.definer.annotations.type.binary;

import zly.rivulet.mysql.definer.outerType.BinaryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLMediumBlob {

    int length();

    class Type implements BinaryType {
        public Type(MySQLMediumBlob mySQLInt) {
        }

        @Override
        public Class<?> getOuterType() {
            return byte[].class;
        }
    }
}
