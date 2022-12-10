package zly.rivulet.mysql.definer.annotations.type.string;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.mysql.definer.outerType.ExactStringType;
import zly.rivulet.mysql.definer.outerType.StringType;

public @interface MySQLText {



    class Type implements StringType {
        public Type(MySQLText mySQLInt) {}

        @Override
        public Class<?> getOuterType() {
            return String.class;
        }
    }
}
