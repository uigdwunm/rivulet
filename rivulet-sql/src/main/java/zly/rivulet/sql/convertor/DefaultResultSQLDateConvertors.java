package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.exception.ParseException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DefaultResultSQLDateConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<Date, String>() {
                    @Override
                    public String convert(Date originData) {
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Date, java.util.Date>() {
                    @Override
                    public java.util.Date convert(Date originData) {
                        return originData;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Date, LocalDateTime>() {
                    @Override
                    public LocalDateTime convert(Date originData) {
                        return originData.toLocalDate().atStartOfDay();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Date, LocalDate>() {
                    @Override
                    public LocalDate convert(Date originData) {
                        return originData.toLocalDate();
                    }
                }
        );
    }
}