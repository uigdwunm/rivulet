package zly.rivulet.mysql.definer.annotations.numeric;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.mysql.definer.outerType.ExactNumericType;

public @interface MySQLInt {
    int maximumDisplayWidth() default 1;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;

    class Type extends ExactNumericType {

        private final int minValue;

        private final long maxValue;

        public Type(int maximumDisplayWidth, BooleanEnum unSigned, BooleanEnum zerofill) {
            super(maximumDisplayWidth, unSigned, zerofill);
            if (this.unSigned) {
                this.minValue = 0;
                this.maxValue = 2L * Integer.MAX_VALUE;
            } else {
                this.minValue = Integer.MIN_VALUE;
                this.maxValue = Integer.MAX_VALUE;
            }
        }

        @Override
        public void registerDefaultConverter(ConvertorManager convertorManager) {

        }
    }
}
