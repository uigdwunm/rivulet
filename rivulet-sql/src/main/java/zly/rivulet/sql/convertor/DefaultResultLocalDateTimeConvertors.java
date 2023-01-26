package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;

import java.time.*;

public interface DefaultResultLocalDateTimeConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<LocalDateTime, String>() {
                    @Override
                    public String convert(LocalDateTime originData) {
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<LocalDateTime, java.util.Date>() {
                    @Override
                    public java.util.Date convert(LocalDateTime originData) {
                        ZoneId zoneId = ZoneOffset.systemDefault();
                        ZonedDateTime zonedDateTime = ZonedDateTime.of(originData, zoneId);

                        Instant instant = zonedDateTime.toInstant();
                        return new java.util.Date(instant.toEpochMilli());
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<LocalDateTime, LocalDate>() {
                    @Override
                    public LocalDate convert(LocalDateTime originData) {
                        return originData.toLocalDate();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<LocalDateTime, LocalTime>() {
                    @Override
                    public LocalTime convert(LocalDateTime originData) {
                        return originData.toLocalTime();
                    }
                }
        );
    }
}