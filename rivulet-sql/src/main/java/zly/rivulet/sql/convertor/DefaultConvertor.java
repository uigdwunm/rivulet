package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.StatementConvertor;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.sql.utils.SQLConstant;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DefaultConvertor {
    public static void registerDefault(ConvertorManager convertorManager) {
        // 注册语句转换器(用于将入参转换成语句)
        registerStatementConvertor(convertorManager);

        // 注册结果转换器(用于将查询结果转换成语句)
        registerBigIntegerConvertor(convertorManager);
        registerLongConvertor(convertorManager);
    }

    private static void registerStatementConvertor(ConvertorManager convertorManager) {
        convertorManager.registerStatementConvertor(
            new StatementConvertor<String>() {
                @Override
                public String convert(String originData) {
                    return originData;
                }
            }
        );
        convertorManager.registerStatementConvertor(
            new StatementConvertor<Integer>() {
                @Override
                public String convert(Integer originData) {
                    if (originData == null) {
                        return SQLConstant.NULL_STATEMENT;
                    }
                    return originData.toString();
                }
            }
        );
        convertorManager.registerStatementConvertor(
            new StatementConvertor<Integer>(int.class) {
                @Override
                public String convert(Integer originData) {
                    if (originData == null) {
                        return SQLConstant.NULL_STATEMENT;
                    }
                    return originData.toString();
                }
            }
        );

        convertorManager.registerStatementConvertor(
            new StatementConvertor<Long>() {
                @Override
                public String convert(Long originData) {
                    if (originData == null) {
                        return SQLConstant.NULL_STATEMENT;
                    }
                    return originData.toString();
                }
            }
        );
        convertorManager.registerStatementConvertor(
            new StatementConvertor<Long>(long.class) {
                @Override
                public String convert(Long originData) {
                    if (originData == null) {
                        return SQLConstant.NULL_STATEMENT;
                    }
                    return originData.toString();
                }
            }
        );
    }

    private static void registerBigIntegerConvertor(ConvertorManager convertorManager) {
        // to self
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, BigInteger>() {
                @Override
                public BigInteger convert(BigInteger originData) {
                    return originData;
                }
            }
        );
        // to long
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Long>(BigInteger.class, long.class) {
                @Override
                public Long convert(BigInteger originData) {
                    return originData.longValueExact();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Long>() {
                @Override
                public Long convert(BigInteger originData) {
                    return originData.longValueExact();
                }
            }
        );
        // to int
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Integer>(BigInteger.class, int.class) {
                @Override
                public Integer convert(BigInteger originData) {
                    return originData.intValueExact();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Integer>() {
                @Override
                public Integer convert(BigInteger originData) {
                    return originData.intValueExact();
                }
            }
        );
        // to byte
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Byte>(BigInteger.class, byte.class) {
                @Override
                public Byte convert(BigInteger originData) {
                    return originData.byteValueExact();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Byte>() {
                @Override
                public Byte convert(BigInteger originData) {
                    return originData.byteValueExact();
                }
            }
        );
        // to double
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Double>(BigInteger.class, double.class) {
                @Override
                public Double convert(BigInteger originData) {
                    return originData.doubleValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Double>() {
                @Override
                public Double convert(BigInteger originData) {
                    return originData.doubleValue();
                }
            }
        );
        // to float
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Float>(BigInteger.class, float.class) {
                @Override
                public Float convert(BigInteger originData) {
                    return originData.floatValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Float>() {
                @Override
                public Float convert(BigInteger originData) {
                    return originData.floatValue();
                }
            }
        );
        // to boolean
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Boolean>(BigInteger.class, boolean.class) {
                @Override
                public Boolean convert(BigInteger originData) {
                    return originData.compareTo(BigInteger.ZERO) > 0;
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, Boolean>() {
                @Override
                public Boolean convert(BigInteger originData) {
                    return originData.compareTo(BigInteger.ZERO) > 0;
                }
            }
        );
        // to string
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, String>() {
                @Override
                public String convert(BigInteger originData) {
                    return originData.toString();
                }
            }
        );
        // to BigDecimal
        convertorManager.registerResultConvertor(
            new Convertor<BigInteger, BigDecimal>() {
                @Override
                public BigDecimal convert(BigInteger originData) {
                    return new BigDecimal(originData);
                }
            }
        );
    }

    private static void registerLongConvertor(ConvertorManager convertorManager) {
        // to self
        convertorManager.registerResultConvertor(
            new Convertor<Long, Long>() {
                @Override
                public Long convert(Long originData) {
                    return originData;
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<Long, Long>(Long.class, long.class) {
                @Override
                public Long convert(Long originData) {
                    return originData;
                }
            }
        );
        // to int
        convertorManager.registerResultConvertor(
            new Convertor<Long, Integer>(Long.class, int.class) {
                @Override
                public Integer convert(Long originData) {
                    return originData.intValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<Long, Integer>() {
                @Override
                public Integer convert(Long originData) {
                    return originData.intValue();
                }
            }
        );
        // to byte
        convertorManager.registerResultConvertor(
            new Convertor<Long, Byte>(Long.class, byte.class) {
                @Override
                public Byte convert(Long originData) {
                    return originData.byteValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<Long, Byte>() {
                @Override
                public Byte convert(Long originData) {
                    return originData.byteValue();
                }
            }
        );
        // to double
        convertorManager.registerResultConvertor(
            new Convertor<Long, Double>(Long.class, double.class) {
                @Override
                public Double convert(Long originData) {
                    return originData.doubleValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<Long, Double>() {
                @Override
                public Double convert(Long originData) {
                    return originData.doubleValue();
                }
            }
        );
        // to float
        convertorManager.registerResultConvertor(
            new Convertor<Long, Float>(Long.class, float.class) {
                @Override
                public Float convert(Long originData) {
                    return originData.floatValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<Long, Float>() {
                @Override
                public Float convert(Long originData) {
                    return originData.floatValue();
                }
            }
        );
        // to boolean
        convertorManager.registerResultConvertor(
            new Convertor<Long, Boolean>(Long.class, boolean.class) {
                @Override
                public Boolean convert(Long originData) {
                    return originData.compareTo(Constant.LONG_ZERO) > 0;
                }
            }
        );
        convertorManager.registerResultConvertor(
            new Convertor<Long, Boolean>() {
                @Override
                public Boolean convert(Long originData) {
                    return originData.compareTo(Constant.LONG_ZERO) > 0;
                }
            }
        );
        // to string
        convertorManager.registerResultConvertor(
            new Convertor<Long, String>() {
                @Override
                public String convert(Long originData) {
                    return originData.toString();
                }
            }
        );
        // to BigDecimal
        convertorManager.registerResultConvertor(
            new Convertor<Long, BigDecimal>() {
                @Override
                public BigDecimal convert(Long originData) {
                    return new BigDecimal(originData);
                }
            }
        );
        // to BigInteger
        convertorManager.registerResultConvertor(
            new Convertor<Long, BigInteger>(Long.class, BigInteger.class) {
                @Override
                public BigInteger convert(Long originData) {
                    return new BigInteger(originData.toString());
                }
            }
        );
    }
}