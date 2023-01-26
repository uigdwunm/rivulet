package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.mysql.definer.outerType.FloatingPointNumericType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MySQLDouble {

    int M();

    int D() default 0;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;

    class Type extends FloatingPointNumericType {

//        private final BigInteger minValue;
//
//        private final BigInteger maxValue;

        private final Class<?> jdbcType;

        public Type(MySQLDouble mySQLDouble) {
            super(mySQLDouble.M(), mySQLDouble.D(), mySQLDouble.unSigned(), mySQLDouble.zerofill());
            this.jdbcType = Double.class;
        }

        @Override
        public Class<?> getOuterType() {
            return this.jdbcType;
        }

    }
}
