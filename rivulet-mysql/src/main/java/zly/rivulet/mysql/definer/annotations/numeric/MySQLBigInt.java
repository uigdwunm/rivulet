package zly.rivulet.mysql.definer.annotations.numeric;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.mysql.definer.outerType.ExactNumericType;

import java.math.BigInteger;

public @interface MySQLBigInt {
    int maximumDisplayWidth() default 1;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;


    class Type extends ExactNumericType {

        private final BigInteger minValue;

        private final BigInteger maxValue;

        public Type(int maximumDisplayWidth, BooleanEnum unSigned, BooleanEnum zerofill) {
            super(maximumDisplayWidth, unSigned, zerofill);
            if (this.unSigned) {
                this.minValue = BigInteger.ZERO;
                this.maxValue = new BigInteger("2").pow(64).subtract(BigInteger.ONE);
            } else {
                this.minValue = new BigInteger("-2").pow(63);
                this.maxValue = new BigInteger("2").pow(63).subtract(BigInteger.ONE);
            }
        }

        @Override
        public void registerDefaultConverter(ConvertorManager convertorManager) {
            convertorManager.register(
                new Convertor<BigInteger, MySQLBigInt.Type>() {
                    @Override
                    public BigInteger convertToJavaType(Object outerValue) {
                        // TODO
                        return null;
                    }

                    @Override
                    public String convertToStatement(BigInteger innerValue) {
                        return innerValue.toString();
                    }

                    @Override
                    public Class<BigInteger> getJavaType() {
                        return BigInteger.class;
                    }

                    @Override
                    public Class<Type> getOriginOuterType() {
                        return Type.class;
                    }
                }
            );
        }

    }
}
