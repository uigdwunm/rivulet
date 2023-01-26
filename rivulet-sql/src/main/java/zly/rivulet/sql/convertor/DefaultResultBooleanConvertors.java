package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.utils.Constant;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface DefaultResultBooleanConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, String>() {
                    @Override
                    public String convert(Boolean originData) {
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Boolean>(Boolean.class, boolean.class) {
                    @Override
                    public Boolean convert(Boolean originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Byte>() {
                    @Override
                    public Byte convert(Boolean originData) {
                        return originData ? Constant.BYTE_ONE : Constant.BYTE_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Byte>(Boolean.class, byte.class) {
                    @Override
                    public Byte convert(Boolean originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData ? Constant.BYTE_ONE : Constant.BYTE_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Short>() {
                    @Override
                    public Short convert(Boolean originData) {
                        return originData ? Constant.SHORT_ONE : Constant.SHORT_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Short>(Boolean.class, short.class) {
                    @Override
                    public Short convert(Boolean originData) {
                        return originData ? Constant.SHORT_ONE : Constant.SHORT_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Integer>() {
                    @Override
                    public Integer convert(Boolean originData) {
                        return originData ? Constant.INTEGER_ONE : Constant.INTEGER_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Integer>(Boolean.class, int.class) {
                    @Override
                    public Integer convert(Boolean originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData ? Constant.INTEGER_ONE : Constant.INTEGER_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Long>() {
                    @Override
                    public Long convert(Boolean originData) {
                        return originData ? Constant.LONG_ONE : Constant.LONG_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Long>(Boolean.class, long.class) {
                    @Override
                    public Long convert(Boolean originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData ? Constant.LONG_ONE : Constant.LONG_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Float>() {
                    @Override
                    public Float convert(Boolean originData) {
                        return originData ? Constant.FLOAT_ONE : Constant.FLOAT_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Float>(Boolean.class, float.class) {
                    @Override
                    public Float convert(Boolean originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData ? Constant.FLOAT_ONE : Constant.FLOAT_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Double>() {
                    @Override
                    public Double convert(Boolean originData) {
                        return originData ? Constant.DOUBLE_ONE : Constant.DOUBLE_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, Double>(Boolean.class, double.class) {
                    @Override
                    public Double convert(Boolean originData) {
                        if (originData == null) {
                            throw ParseException.convertNullToBase();
                        }
                        return originData ? Constant.DOUBLE_ONE : Constant.DOUBLE_ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, BigInteger>() {
                    @Override
                    public BigInteger convert(Boolean originData) {
                        return originData ? BigInteger.ONE : BigInteger.ZERO;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Boolean, BigDecimal>() {
                    @Override
                    public BigDecimal convert(Boolean originData) {
                        return originData ? BigDecimal.ONE : BigDecimal.ZERO;
                    }
                }
        );
    }
}