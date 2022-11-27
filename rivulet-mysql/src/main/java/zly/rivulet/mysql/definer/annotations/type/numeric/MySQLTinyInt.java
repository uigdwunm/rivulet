package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.mysql.definer.outerType.ExactNumericType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLTinyInt {
    // TODO
    int maximumDisplayWidth() default 1;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;

    class Type extends ExactNumericType {

        private final int minValue;

        private final long maxValue;

        public Type(MySQLTinyInt mySQLTinyInt) {
            super(mySQLTinyInt.maximumDisplayWidth(), mySQLTinyInt.unSigned(), mySQLTinyInt.zerofill());
            if (this.unSigned) {
                this.minValue = 0;
                this.maxValue = 255;
            } else {
                this.minValue = -128;
                this.maxValue = 127;
            }
        }

        public static void registerConvertors(ConvertorManager convertorManager) {
            new Convertor<Boolean, Type>(Boolean.class, Type.class) {

                @Override
                public Boolean convertToJavaType(Object outerValue) {
                    if (outerValue == null) {
                        return null;
                    }
                    Integer value = (Integer) outerValue;
                    return value > 0;
                }

                @Override
                public String convertToStatement(Boolean innerValue) {
                    if (innerValue == null) {
                        return null;
                    }
                    return innerValue ? Constant.ONE_STR : Constant.ZERO_STR;
                }
            };
        }
    }
}
