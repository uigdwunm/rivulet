package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.exception.ParseException;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface DefaultResultLongConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, String>() {
                    @Override
                    public String convert(Long originData) {
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, Boolean>() {
                    @Override
                    public Boolean convert(Long originData) {
                        if (originData == null) {
                            return null;
                        }
                        return originData > 0;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, Boolean>(Long.class, boolean.class) {
                    @Override
                    public Boolean convert(Long originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData > 0;
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
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, Byte>(Long.class, byte.class) {
                    @Override
                    public Byte convert(Long originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.byteValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, Short>() {
                    @Override
                    public Short convert(Long originData) {
                        return originData.shortValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, Short>(Long.class, short.class) {
                    @Override
                    public Short convert(Long originData) {
                        return originData.shortValue();
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
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, Integer>(Long.class, int.class) {
                    @Override
                    public Integer convert(Long originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.intValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, Long>(Long.class, long.class) {
                    @Override
                    public Long convert(Long originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData;
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
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, Float>(Long.class, float.class) {
                    @Override
                    public Float convert(Long originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.floatValue();
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
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, Double>(Long.class, double.class) {
                    @Override
                    public Double convert(Long originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData.doubleValue();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, BigInteger>() {
                    @Override
                    public BigInteger convert(Long originData) {
                        return BigInteger.valueOf(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Long, BigDecimal>() {
                    @Override
                    public BigDecimal convert(Long originData) {
                        return new BigDecimal(originData);
                    }
                }
        );
    }
}