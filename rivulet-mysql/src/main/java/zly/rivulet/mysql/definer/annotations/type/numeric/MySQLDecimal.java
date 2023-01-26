package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.mysql.definer.outerType.FixedPointNumericType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLDecimal {

    int M();

    int D() default 0;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;

    class Type extends FixedPointNumericType {

//        private final BigInteger minValue;
//
//        private final BigInteger maxValue;

        private final Class<?> jdbcType;

        public Type(MySQLDecimal anno) {
            super(anno.M(), anno.D(), anno.unSigned(), anno.zerofill());
            this.jdbcType = BigDecimal.class;
        }

        @Override
        public Class<?> getOuterType() {
            return this.jdbcType;
        }

    }
}
