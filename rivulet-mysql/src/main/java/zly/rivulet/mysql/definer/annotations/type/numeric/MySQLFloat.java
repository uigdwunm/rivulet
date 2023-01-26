package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.mysql.definer.outerType.FloatingPointNumericType;
import zly.rivulet.mysql.definer.outerType.NumericType;
import zly.rivulet.mysql.definer.outerType.feature.FloatingPoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLFloat {

    int M();

    int D() default 0;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;


    class Type extends FloatingPointNumericType {

//        private final BigInteger minValue;
//
//        private final BigInteger maxValue;

        private final Class<?> jdbcType;

        public Type(MySQLFloat mySQLBigInt) {
            super(mySQLBigInt.M(), mySQLBigInt.D(), mySQLBigInt.unSigned(), mySQLBigInt.zerofill());
            this.jdbcType = Float.class;
        }

        @Override
        public Class<?> getOuterType() {
            return this.jdbcType;
        }

    }
}
