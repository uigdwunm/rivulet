package zly.rivulet.mysql.convertor;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.outerType.SelfType;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.mysql.util.MySQLConstant;

import java.math.BigInteger;

public abstract class MySQLSelfTypeConvertor<T> extends Convertor<T, SelfType> {
    private MySQLSelfTypeConvertor(Class<T> javaType) {
        super(javaType, SelfType.class);
    }

    @Override
    public T convertToJavaType(Object outerValue) {
        throw UnbelievableException.unbelievable();
    }


    public static void registerConvertors(ConvertorManager convertorManager) {

        convertorManager.register(
            new MySQLSelfTypeConvertor<String>(String.class) {
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
            new MySQLSelfTypeConvertor<Integer>(int.class) {
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
            new MySQLSelfTypeConvertor<Integer>(Integer.class) {
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
            new MySQLSelfTypeConvertor<Long>(long.class) {
                @Override
                public String convertToStatement(Long innerValue) {
                    if (innerValue == null) {
                        return MySQLConstant.MYSQL_NULL;
                    }
                    return innerValue.toString();
                }
            }

        );
        convertorManager.register(
            new MySQLSelfTypeConvertor<Long>(Long.class) {
                @Override
                public String convertToStatement(Long innerValue) {
                    if (innerValue == null) {
                        return MySQLConstant.MYSQL_NULL;
                    }
                    return innerValue.toString();
                }
            }

        );
        convertorManager.register(
            new MySQLSelfTypeConvertor<BigInteger>(BigInteger.class) {
                @Override
                public String convertToStatement(BigInteger innerValue) {
                    if (innerValue == null) {
                        return MySQLConstant.MYSQL_NULL;
                    }
                    return innerValue.toString();
                }
            }
        );
    }
}
