package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.mysql.definer.outerType.ExactNumericType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLMediumInt {
    // TODO
    int maximumDisplayWidth() default 1;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;

    class Type extends ExactNumericType {

        private final int minValue;

        private final long maxValue;

        private final Class<?> jdbcType;

        public Type(MySQLMediumInt anno) {
            super(anno.maximumDisplayWidth(), anno.unSigned(), anno.zerofill());
            if (this.unSigned) {
                this.minValue = 0;
                this.maxValue = 16777215;
            } else {
                this.minValue = -8388608;
                this.maxValue = 8388607;
            }
            this.jdbcType = Integer.class;
        }

        @Override
        public Class<?> getOuterType() {
            return jdbcType;
        }
    }
}
