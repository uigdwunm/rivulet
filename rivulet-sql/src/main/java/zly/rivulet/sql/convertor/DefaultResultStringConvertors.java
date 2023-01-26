package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.exception.ParseException;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface DefaultResultStringConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Boolean>() {
                    @Override
                    public Boolean convert(String originData) {
                        return Boolean.valueOf(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Boolean>(String.class, boolean.class) {
                    @Override
                    public Boolean convert(String originData) {
                        return Boolean.parseBoolean(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Character>() {
                    @Override
                    public Character convert(String originData) {
                        if (originData == null) {
                            return null;
                        }
                        if (originData.length() != 1) {
                            throw ParseException.convertFailed(originData, Character.class);
                        }
                        return originData.charAt(0);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Character>(String.class, char.class) {
                    @Override
                    public Character convert(String originData) {
                        if (originData == null) {
                            return null;
                        }
                        if (originData.length() != 1) {
                            throw ParseException.convertFailed(originData, Character.class);
                        }
                        return originData.charAt(0);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Byte>() {
                    @Override
                    public Byte convert(String originData) {
                        return Byte.valueOf(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Byte>(String.class, byte.class) {
                    @Override
                    public Byte convert(String originData) {
                        return Byte.parseByte(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Short>() {
                    @Override
                    public Short convert(String originData) {
                        return Short.valueOf(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Short>(String.class, short.class) {
                    @Override
                    public Short convert(String originData) {
                        return Short.parseShort(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Integer>() {
                    @Override
                    public Integer convert(String originData) {
                        return Integer.valueOf(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Integer>(String.class, int.class) {
                    @Override
                    public Integer convert(String originData) {
                        return Integer.parseInt(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Long>() {
                    @Override
                    public Long convert(String originData) {
                        return Long.valueOf(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Long>(String.class, long.class) {
                    @Override
                    public Long convert(String originData) {
                        return Long.parseLong(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, BigInteger>() {
                    @Override
                    public BigInteger convert(String originData) {
                        return new BigInteger(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Float>() {
                    @Override
                    public Float convert(String originData) {
                        return Float.valueOf(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Float>(String.class, float.class) {
                    @Override
                    public Float convert(String originData) {
                        return Float.parseFloat(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Double>() {
                    @Override
                    public Double convert(String originData) {
                        return Double.valueOf(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, Double>(String.class, double.class) {
                    @Override
                    public Double convert(String originData) {
                        return Double.parseDouble(originData);
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<String, BigDecimal>() {
                    @Override
                    public BigDecimal convert(String originData) {
                        return new BigDecimal(originData);
                    }
                }
        );
    }
}