package zly.rivulet.mysql.definer.annotations.type.string;

import zly.rivulet.mysql.definer.outerType.VariableStringType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MySQLVarchar {

    int length();

    class Type extends VariableStringType {

        public Type(MySQLVarchar mySQLInt) {
            super(mySQLInt.length());
        }

        @Override
        public Class<?> getOuterType() {
            return String.class;
        }
    }
}
