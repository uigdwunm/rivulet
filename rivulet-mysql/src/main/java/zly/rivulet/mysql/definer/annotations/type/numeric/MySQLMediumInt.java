package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.mysql.definer.outerType.ExactNumericType;

public @interface MySQLMediumInt {
    // TODO
    int maximumDisplayWidth() default 1;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;

    class Type extends ExactNumericType {

        private final int minValue;

        private final long maxValue;

        private final Class<?> jdbcType;

        public Type(int maximumDisplayWidth, BooleanEnum unSigned, BooleanEnum zerofill) {
            super(maximumDisplayWidth, unSigned, zerofill);
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
            return null;
        }
    }
}
