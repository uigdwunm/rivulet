package zly.rivulet.mysql.definer.annotations.type.string;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.mysql.definer.outerType.ExactStringType;
import zly.rivulet.mysql.definer.outerType.StringType;

public @interface MySQLText {



    class Type implements StringType {
        public Type(MySQLText mySQLInt) {}

        public static void registerConvertors(ConvertorManager convertorManager) {
            convertorManager.register(new Convertor<String, Type>(String.class, Type.class) {
                @Override
                public String convertToJavaType(Object outerValue) {
                    return (String) outerValue;
                }

                @Override
                public String convertToStatement(String innerValue) {
                    return "'" + innerValue + "'";
                }
            });
        }
    }
}
