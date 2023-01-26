package zly.rivulet.sql.convertor;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface DefaultResultSQLTimeConvertors {

    static void register(ConvertorManager convertorManager) {
        convertorManager.registerResultConvertor(
                new ResultConvertor<Time, String>() {
                    @Override
                    public String convert(Time originData) {
                        return originData.toString();
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Time, java.util.Date>() {
                    @Override
                    public java.util.Date convert(Time originData) {
                        return originData;
                    }
                }
        );
        convertorManager.registerResultConvertor(
                new ResultConvertor<Time, LocalTime>() {
                    @Override
                    public LocalTime convert(Time originData) {
                        return originData.toLocalTime();
                    }
                }
        );
    }
}