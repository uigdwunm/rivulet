package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface DefaultResultSQLTimestampConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<Timestamp, String>() {
                    @Override
                    public String convert(Timestamp originData) {
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Timestamp, java.util.Date>() {
                    @Override
                    public java.util.Date convert(Timestamp originData) {
                        return originData;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Timestamp, LocalDateTime>() {
                    @Override
                    public LocalDateTime convert(Timestamp originData) {
                        return originData.toLocalDateTime();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Timestamp, LocalDate>() {
                    @Override
                    public LocalDate convert(Timestamp originData) {
                        return originData.toLocalDateTime().toLocalDate();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Timestamp, LocalTime>() {
                    @Override
                    public LocalTime convert(Timestamp originData) {
                        return originData.toLocalDateTime().toLocalTime();
                    }
                }
        );
    }
}