package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.StatementConvertor;
import zly.rivulet.sql.utils.SQLConstant;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public interface DefaultStatementConvertors {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    static void register(ConvertorManager convertorManager) {
        convertorManager.registerStatementConvertor(
                new StatementConvertor<String>() {
                    @Override
                    public String convert(String originData) {
                        return "'" + originData + "'";
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Character>() {
                    @Override
                    public String convert(Character originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Character>(char.class) {
                    @Override
                    public String convert(Character originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Boolean>() {
                    @Override
                    public String convert(Boolean originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Boolean>(boolean.class) {
                    @Override
                    public String convert(Boolean originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Byte>() {
                    @Override
                    public String convert(Byte originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Byte>(byte.class) {
                    @Override
                    public String convert(Byte originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Short>() {
                    @Override
                    public String convert(Short originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Short>(short.class) {
                    @Override
                    public String convert(Short originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
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
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Float>() {
                    @Override
                    public String convert(Float originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Float>(float.class) {
                    @Override
                    public String convert(Float originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Double>() {
                    @Override
                    public String convert(Double originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Double>(double.class) {
                    @Override
                    public String convert(Double originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<BigDecimal>() {
                    @Override
                    public String convert(BigDecimal originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }

                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<BigInteger>() {
                    @Override
                    public String convert(BigInteger originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }

                        return originData.toString();
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<Date>() {
                    @Override
                    public String convert(Date originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }

                        return "'" + simpleDateFormat.format(originData) + "'";
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<LocalDate>() {
                    @Override
                    public String convert(LocalDate originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return "'" + DateTimeFormatter.ISO_LOCAL_DATE.format(originData) + "'";
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<LocalTime>() {
                    @Override
                    public String convert(LocalTime originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return "'" + DateTimeFormatter.ISO_LOCAL_TIME.format(originData) + "'";
                    }
                }
        );
        convertorManager.registerStatementConvertor(
                new StatementConvertor<LocalDateTime>() {
                    @Override
                    public String convert(LocalDateTime originData) {
                        if (originData == null) {
                            return SQLConstant.NULL_STATEMENT;
                        }
                        return "'" + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(originData) + "'";
                    }
                }
        );

    }
}
