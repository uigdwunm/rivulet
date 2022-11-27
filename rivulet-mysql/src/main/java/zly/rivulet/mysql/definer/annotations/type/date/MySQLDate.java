package zly.rivulet.mysql.definer.annotations.type.date;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.outerType.OriginOuterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLDate {


    class Type implements OriginOuterType {
        public Type(MySQLDate mySQLDate) {
        }

        public static void registerConvertors(ConvertorManager convertorManager) {
            convertorManager.register(
                new Convertor<Date, Type>(Date.class, Type.class) {

                    @Override
                    public Date convertToJavaType(Object outerValue) {
                        return (Date) outerValue;
                    }

                    @Override
                    public String convertToStatement(Date innerValue) {
                        return null;
                    }
                }
            );

            convertorManager.register(
                new Convertor<LocalDate, Type>(LocalDate.class, Type.class) {

                    @Override
                    public LocalDate convertToJavaType(Object outerValue) {
                        return null;
                    }

                    @Override
                    public String convertToStatement(LocalDate innerValue) {
                        return null;
                    }
                }
            );
        }
    }
}
