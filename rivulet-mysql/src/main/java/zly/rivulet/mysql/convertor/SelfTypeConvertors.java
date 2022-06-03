package zly.rivulet.mysql.convertor;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.definer.outerType.SelfType;
import zly.rivulet.mysql.util.MySQLConstant;

import java.math.BigInteger;

public class SelfTypeConvertors {

    public static void registerConvertors(ConvertorManager convertorManager) {
        convertorManager.register(
            new Convertor<String, SelfType>(String.class, SelfType.class) {
                @Override
                public String convertToJavaType(Object outerValue) {
                    throw UnbelievableException.unbelievable();
                }

                @Override
                public String convertToStatement(String innerValue) {
                    if (innerValue == null) {
                        return MySQLConstant.MYSQL_NULL;
                    }
                    return "'" + innerValue + "'";
                }
            }
        );

        convertorManager.register(
            new Convertor<Integer, SelfType>(Integer.class, SelfType.class) {
                @Override
                public Integer convertToJavaType(Object outerValue) {
                    throw UnbelievableException.unbelievable();
                }

                @Override
                public String convertToStatement(Integer innerValue) {
                    if (innerValue == null) {
                        return MySQLConstant.MYSQL_NULL;
                    }
                    return innerValue.toString();
                }
            }

        );
        convertorManager.register(
            new Convertor<BigInteger, SelfType>(BigInteger.class, SelfType.class) {
                @Override
                public BigInteger convertToJavaType(Object outerValue) {
                    throw UnbelievableException.unbelievable();
                }

                @Override
                public String convertToStatement(BigInteger innerValue) {
                    return innerValue.toString();
                }
            }
        );
    }

}
