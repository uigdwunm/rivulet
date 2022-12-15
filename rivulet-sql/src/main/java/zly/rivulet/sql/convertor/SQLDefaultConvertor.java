package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.StatementConvertor;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.sql.utils.SQLConstant;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

public class SQLDefaultConvertor {
    public static void registerDefault(ConvertorManager convertorManager) {
        // 注册语句转换器(用于将入参转换成语句)
        registerStatementConvertor(convertorManager);

        // 注册结果转换器(用于将查询结果转换成语句)
        registerStringConvertor(convertorManager);
        registerBigIntegerConvertor(convertorManager);
        registerLongConvertor(convertorManager);
        registerIntegerConvertor(convertorManager);
        registerDateConvertor(convertorManager);
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

    private static void registerStringConvertor(ConvertorManager convertorManager) {
        // to self
        convertorManager.registerResultConvertor(
            new ResultConvertor<String, String>() {
                @Override
                public String convert(String originData) {
                    return originData;
                }
            }
        );
    }

    private static void registerBigIntegerConvertor(ConvertorManager convertorManager) {
        // to self
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, BigInteger>() {
                @Override
                public BigInteger convert(BigInteger originData) {
                    return originData;
                }
            }
        );
        // to long
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Long>(BigInteger.class, long.class) {
                @Override
                public Long convert(BigInteger originData) {
                    return originData.longValueExact();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Long>() {
                @Override
                public Long convert(BigInteger originData) {
                    return originData.longValueExact();
                }
            }
        );
        // to int
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Integer>(BigInteger.class, int.class) {
                @Override
                public Integer convert(BigInteger originData) {
                    return originData.intValueExact();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Integer>() {
                @Override
                public Integer convert(BigInteger originData) {
                    return originData.intValueExact();
                }
            }
        );
        // to byte
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Byte>(BigInteger.class, byte.class) {
                @Override
                public Byte convert(BigInteger originData) {
                    return originData.byteValueExact();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Byte>() {
                @Override
                public Byte convert(BigInteger originData) {
                    return originData.byteValueExact();
                }
            }
        );
        // to double
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Double>(BigInteger.class, double.class) {
                @Override
                public Double convert(BigInteger originData) {
                    return originData.doubleValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Double>() {
                @Override
                public Double convert(BigInteger originData) {
                    return originData.doubleValue();
                }
            }
        );
        // to float
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Float>(BigInteger.class, float.class) {
                @Override
                public Float convert(BigInteger originData) {
                    return originData.floatValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Float>() {
                @Override
                public Float convert(BigInteger originData) {
                    return originData.floatValue();
                }
            }
        );
        // to boolean
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Boolean>(BigInteger.class, boolean.class) {
                @Override
                public Boolean convert(BigInteger originData) {
                    return originData.compareTo(BigInteger.ZERO) > 0;
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, Boolean>() {
                @Override
                public Boolean convert(BigInteger originData) {
                    return originData.compareTo(BigInteger.ZERO) > 0;
                }
            }
        );
        // to string
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, String>() {
                @Override
                public String convert(BigInteger originData) {
                    return originData.toString();
                }
            }
        );
        // to BigDecimal
        convertorManager.registerResultConvertor(
            new ResultConvertor<BigInteger, BigDecimal>() {
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
            new ResultConvertor<Long, Long>() {
                @Override
                public Long convert(Long originData) {
                    return originData;
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, Long>(Long.class, long.class) {
                @Override
                public Long convert(Long originData) {
                    return originData;
                }
            }
        );
        // to int
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, Integer>(Long.class, int.class) {
                @Override
                public Integer convert(Long originData) {
                    return originData.intValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, Integer>() {
                @Override
                public Integer convert(Long originData) {
                    return originData.intValue();
                }
            }
        );
        // to byte
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, Byte>(Long.class, byte.class) {
                @Override
                public Byte convert(Long originData) {
                    return originData.byteValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, Byte>() {
                @Override
                public Byte convert(Long originData) {
                    return originData.byteValue();
                }
            }
        );
        // to double
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, Double>(Long.class, double.class) {
                @Override
                public Double convert(Long originData) {
                    return originData.doubleValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, Double>() {
                @Override
                public Double convert(Long originData) {
                    return originData.doubleValue();
                }
            }
        );
        // to float
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, Float>(Long.class, float.class) {
                @Override
                public Float convert(Long originData) {
                    return originData.floatValue();
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, Float>() {
                @Override
                public Float convert(Long originData) {
                    return originData.floatValue();
                }
            }
        );
        // to boolean
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, Boolean>(Long.class, boolean.class) {
                @Override
                public Boolean convert(Long originData) {
                    return originData.compareTo(Constant.LONG_ZERO) > 0;
                }
            }
        );
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, Boolean>() {
                @Override
                public Boolean convert(Long originData) {
                    return originData.compareTo(Constant.LONG_ZERO) > 0;
                }
            }
        );
        // to string
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, String>() {
                @Override
                public String convert(Long originData) {
                    return originData.toString();
                }
            }
        );
        // to BigDecimal
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, BigDecimal>() {
                @Override
                public BigDecimal convert(Long originData) {
                    return new BigDecimal(originData);
                }
            }
        );
        // to BigInteger
        convertorManager.registerResultConvertor(
            new ResultConvertor<Long, BigInteger>(Long.class, BigInteger.class) {
                @Override
                public BigInteger convert(Long originData) {
                    return new BigInteger(originData.toString());
                }
            }
        );
    }

    private static void registerIntegerConvertor(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
            new ResultConvertor<Integer, Boolean>(Integer.class, boolean.class) {
                @Override
                public Boolean convert(Integer originData) {
                    if (originData == null) {
                        return null;
                    }
                    return originData > 0;
                }
            }
        );
    }

    private static void registerDateConvertor(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
            new ResultConvertor<java.sql.Date, LocalDate>() {
                @Override
                public LocalDate convert(java.sql.Date originData) {
                    return originData.toLocalDate();
                }
            }
        );

    }
}
