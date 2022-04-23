package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.mysql.definer.outerType.ExactNumericType;

public @interface MySQLSmallInt {
    int maximumDisplayWidth() default 1;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;

    class Type extends ExactNumericType {

        private final int minValue;

        private final int maxValue;

        public Type(int maximumDisplayWidth, BooleanEnum unSigned, BooleanEnum zerofill) {
            super(maximumDisplayWidth, unSigned, zerofill);
            if (this.unSigned) {
                this.minValue = 0;
                this.maxValue = 65535;
            } else {
                this.minValue = -32768;
                this.maxValue = 32767;
            }
        }
    }
}
