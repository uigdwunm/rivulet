package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.exception.ParseException;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface DefaultResultIntegerConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, String>() {
                    @Override
                    public String convert(Integer originData) {
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Boolean>() {
                    @Override
                    public Boolean convert(Integer originData) {
                        if (originData == null) {
                            return null;
                        }
                        return originData > 0;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Boolean>(Integer.class, boolean.class) {
                    @Override
                    public Boolean convert(Integer originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData > 0;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Byte>() {
                    @Override
                    public Byte convert(Integer originData) {
                        return originData.byteValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Byte>(Integer.class, byte.class) {
                    @Override
                    public Byte convert(Integer originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.byteValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Short>() {
                    @Override
                    public Short convert(Integer originData) {
                        return originData.shortValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Short>(Integer.class, short.class) {
                    @Override
                    public Short convert(Integer originData) {
                        return originData.shortValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Integer>(Integer.class, int.class) {
                    @Override
                    public Integer convert(Integer originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Long>() {
                    @Override
                    public Long convert(Integer originData) {
                        return originData.longValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Long>(Integer.class, long.class) {
                    @Override
                    public Long convert(Integer originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.longValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Float>() {
                    @Override
                    public Float convert(Integer originData) {
                        return originData.floatValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Float>(Integer.class, float.class) {
                    @Override
                    public Float convert(Integer originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.floatValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Double>() {
                    @Override
                    public Double convert(Integer originData) {
                        return originData.doubleValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, Double>(Integer.class, double.class) {
                    @Override
                    public Double convert(Integer originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.doubleValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, BigInteger>() {
                    @Override
                    public BigInteger convert(Integer originData) {
                        return BigInteger.valueOf(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Integer, BigDecimal>() {
                    @Override
                    public BigDecimal convert(Integer originData) {
                        return new BigDecimal(originData);
                    }
                }
        );
    }
}