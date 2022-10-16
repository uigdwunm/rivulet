package zly.rivulet.mysql.definer.annotations.type.string;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.mysql.definer.outerType.ExactStringType;

public @interface MySQLChar {

    int length();

    class Type extends ExactStringType {
        public Type(MySQLChar mySQLInt) {
            super(mySQLInt.length());
        }

        public static void registerConvertors(ConvertorManager convertorManager) {
            convertorManager.register(new Convertor<String, Type>(String.class, Type.class) {
                @Override
                public String convertToJavaType(Object outerValue) {
                    // TODO
                    return null;
                }

                @Override
                public String convertToStatement(String innerValue) {
                    return "'" + innerValue + "'";
                }
            });
        }
    }
}
