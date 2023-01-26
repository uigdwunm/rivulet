package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.exception.ParseException;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface DefaultResultDoubleConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, String>() {
                    @Override
                    public String convert(Double originData) {
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Boolean>() {
                    @Override
                    public Boolean convert(Double originData) {
                        if (originData == null) {
                            return null;
                        }
                        return originData > 0F;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Boolean>(Double.class, boolean.class) {
                    @Override
                    public Boolean convert(Double originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData > 0F;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Byte>() {
                    @Override
                    public Byte convert(Double originData) {
                        return originData.byteValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Byte>(Double.class, byte.class) {
                    @Override
                    public Byte convert(Double originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.byteValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Short>() {
                    @Override
                    public Short convert(Double originData) {
                        return originData.shortValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Short>(Double.class, short.class) {
                    @Override
                    public Short convert(Double originData) {
                        return originData.shortValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Integer>() {
                    @Override
                    public Integer convert(Double originData) {
                        return originData.intValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Integer>(Double.class, int.class) {
                    @Override
                    public Integer convert(Double originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.intValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Long>() {
                    @Override
                    public Long convert(Double originData) {
                        return originData.longValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Long>(Double.class, long.class) {
                    @Override
                    public Long convert(Double originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.longValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Float>() {
                    @Override
                    public Float convert(Double originData) {
                        return originData.floatValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Float>(Double.class, float.class) {
                    @Override
                    public Float convert(Double originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.floatValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, Double>(Double.class, double.class) {
                    @Override
                    public Double convert(Double originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, BigInteger>() {
                    @Override
                    public BigInteger convert(Double originData) {
                        return BigInteger.valueOf(originData.longValue());
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Double, BigDecimal>() {
                    @Override
                    public BigDecimal convert(Double originData) {
                        return new BigDecimal(originData);
                    }
                }
        );
    }
}