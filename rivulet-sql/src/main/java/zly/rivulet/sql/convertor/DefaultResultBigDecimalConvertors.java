package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.exception.ParseException;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface DefaultResultBigDecimalConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, String>() {
                    @Override
                    public String convert(BigDecimal originData) {
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Boolean>() {
                    @Override
                    public Boolean convert(BigDecimal originData) {
                        return originData.compareTo(BigDecimal.ZERO) > 0;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Boolean>(BigDecimal.class, boolean.class) {
                    @Override
                    public Boolean convert(BigDecimal originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.compareTo(BigDecimal.ZERO) > 0;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Byte>() {
                    @Override
                    public Byte convert(BigDecimal originData) {
                        return originData.byteValueExact();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Byte>(BigDecimal.class, byte.class) {
                    @Override
                    public Byte convert(BigDecimal originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.byteValueExact();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Short>() {
                    @Override
                    public Short convert(BigDecimal originData) {
                        return originData.shortValueExact();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Short>(BigDecimal.class, short.class) {
                    @Override
                    public Short convert(BigDecimal originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.shortValueExact();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Integer>() {
                    @Override
                    public Integer convert(BigDecimal originData) {
                        return originData.intValueExact();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Integer>(BigDecimal.class, int.class) {
                    @Override
                    public Integer convert(BigDecimal originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.intValueExact();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Long>() {
                    @Override
                    public Long convert(BigDecimal originData) {
                        return originData.longValueExact();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Long>(BigDecimal.class, long.class) {
                    @Override
                    public Long convert(BigDecimal originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.longValueExact();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Float>() {
                    @Override
                    public Float convert(BigDecimal originData) {
                        return originData.floatValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Float>(BigDecimal.class, float.class) {
                    @Override
                    public Float convert(BigDecimal originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.floatValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Double>() {
                    @Override
                    public Double convert(BigDecimal originData) {
                        return originData.doubleValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, Double>(BigDecimal.class, double.class) {
                    @Override
                    public Double convert(BigDecimal originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.doubleValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<BigDecimal, BigInteger>() {
                    @Override
                    public BigInteger convert(BigDecimal originData) {
                        return originData.toBigIntegerExact();
                    }
                }
        );
    }
}