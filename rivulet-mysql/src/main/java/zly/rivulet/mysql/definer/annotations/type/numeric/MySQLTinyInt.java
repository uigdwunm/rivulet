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

        private final Class<?> jdbcType = Integer.class;

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

        @Override
        public Class<?> getOuterType() {
            return jdbcType;
        }
    }
}
