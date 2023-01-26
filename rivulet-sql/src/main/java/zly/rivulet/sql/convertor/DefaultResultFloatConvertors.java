package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.exception.ParseException;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface DefaultResultFloatConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, String>() {
                    @Override
                    public String convert(Float originData) {
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Boolean>() {
                    @Override
                    public Boolean convert(Float originData) {
                        if (originData == null) {
                            return null;
                        }
                        return originData > 0F;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Boolean>(Float.class, boolean.class) {
                    @Override
                    public Boolean convert(Float originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData > 0F;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Byte>() {
                    @Override
                    public Byte convert(Float originData) {
                        return originData.byteValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Byte>(Float.class, byte.class) {
                    @Override
                    public Byte convert(Float originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.byteValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Short>() {
                    @Override
                    public Short convert(Float originData) {
                        return originData.shortValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Short>(Float.class, short.class) {
                    @Override
                    public Short convert(Float originData) {
                        return originData.shortValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Integer>() {
                    @Override
                    public Integer convert(Float originData) {
                        return originData.intValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Integer>(Float.class, int.class) {
                    @Override
                    public Integer convert(Float originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.intValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Long>() {
                    @Override
                    public Long convert(Float originData) {
                        return originData.longValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Long>(Float.class, long.class) {
                    @Override
                    public Long convert(Float originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.longValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Float>(Float.class, float.class) {
                    @Override
                    public Float convert(Float originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Double>() {
                    @Override
                    public Double convert(Float originData) {
                        return originData.doubleValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, Double>(Float.class, double.class) {
                    @Override
                    public Double convert(Float originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.doubleValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, BigInteger>() {
                    @Override
                    public BigInteger convert(Float originData) {
                        return BigInteger.valueOf(originData.longValue());
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Float, BigDecimal>() {
                    @Override
                    public BigDecimal convert(Float originData) {
                        return new BigDecimal(originData);
                    }
                }
        );
    }
}