package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.exception.ParseException;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface DefaultResultBigIntegerConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigInteger, String>() {
                    @Override
                    public String convert(BigInteger originData) {
                        return originData.toString();
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
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigInteger, Boolean>(BigInteger.class, boolean.class) {
                    @Override
                    public Boolean convert(BigInteger originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.compareTo(BigInteger.ZERO) > 0;
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
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigInteger, Byte>(BigInteger.class, byte.class) {
                    @Override
                    public Byte convert(BigInteger originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.byteValueExact();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigInteger, Short>() {
                    @Override
                    public Short convert(BigInteger originData) {
                        return originData.shortValueExact();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigInteger, Short>(BigInteger.class, short.class) {
                    @Override
                    public Short convert(BigInteger originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.shortValueExact();
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
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigInteger, Integer>(BigInteger.class, int.class) {
                    @Override
                    public Integer convert(BigInteger originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.intValueExact();
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
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigInteger, Long>(BigInteger.class, long.class) {
                    @Override
                    public Long convert(BigInteger originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.longValueExact();
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
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigInteger, Float>(BigInteger.class, float.class) {
                    @Override
                    public Float convert(BigInteger originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.floatValue();
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
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigInteger, Double>(BigInteger.class, double.class) {
                    @Override
                    public Double convert(BigInteger originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.doubleValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigInteger, BigDecimal>() {
                    @Override
                    public BigDecimal convert(BigInteger originData) {
                        return new BigDecimal(originData);
                    }
                }
        );
    }
}