package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.mysql.definer.outerType.ExactNumericType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigInteger;

@Retention(RetentionPolicy.RUNTIME)
public @interface MySQLBigInt {
    int maximumDisplayWidth() default 1;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;


    class Type extends ExactNumericType {

        private final BigInteger minValue;

        private final BigInteger maxValue;

        public Type(MySQLBigInt mySQLBigInt) {
            super(mySQLBigInt.maximumDisplayWidth(), mySQLBigInt.unSigned(), mySQLBigInt.zerofill());
            if (this.unSigned) {
                this.minValue = BigInteger.ZERO;
                this.maxValue = new BigInteger("2").pow(64).subtract(BigInteger.ONE);
            } else {
                this.minValue = new BigInteger("-2").pow(63);
                this.maxValue = new BigInteger("2").pow(63).subtract(BigInteger.ONE);
            }
        }

        public static void registerConvertors(ConvertorManager convertorManager) {
            convertorManager.register(
                new Convertor<BigInteger, MySQLBigInt.Type>(BigInteger.class, Type.class) {
                    @Override
                    public BigInteger convertToJavaType(Object outerValue) {
                        // TODO
                        return null;
                    }

                    @Override
                    public String convertToStatement(BigInteger innerValue) {
                        return innerValue.toString();
                    }

                }
            );

            convertorManager.register(
                new Convertor<Long, MySQLBigInt.Type>(Long.class, Type.class) {

                    @Override
                    public Long convertToJavaType(Object outerValue) {
                        return null;
                    }

                    @Override
                    public String convertToStatement(Long innerValue) {
                        return innerValue.toString();
                    }
                }
            );

            convertorManager.register(
                new Convertor<Long, MySQLBigInt.Type>(long.class, Type.class) {

                    @Override
                    public Long convertToJavaType(Object outerValue) {
                        return null;
                    }

                    @Override
                    public String convertToStatement(Long innerValue) {
                        return innerValue.toString();
                    }
                }
            );
        }

    }
}
