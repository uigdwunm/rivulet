package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.mysql.definer.outerType.ExactNumericType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MySQLInt {

    int maximumDisplayWidth() default 1;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;


    class Type extends ExactNumericType {

        private final int minValue;

        private final long maxValue;

        public Type(MySQLInt mySQLInt) {
            super(mySQLInt.maximumDisplayWidth(), mySQLInt.unSigned(), mySQLInt.zerofill());
            if (this.unSigned) {
                this.minValue = 0;
                this.maxValue = 2L * Integer.MAX_VALUE;
            } else {
                this.minValue = Integer.MIN_VALUE;
                this.maxValue = Integer.MAX_VALUE;
            }
        }

        public static void registerConvertors(ConvertorManager convertorManager) {
            convertorManager.register(new Convertor<Integer, Type>(Integer.class, MySQLInt.Type.class) {
                @Override
                public Integer convertToJavaType(Object outerValue) {
                    // TODO
                    return null;
                }

                @Override
                public String convertToStatement(Integer innerValue) {
                    return innerValue.toString();
                }
            });
        }
    }
}
