package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.StatementConvertor;
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

        private final Class<?> jdbcType;

        public Type(MySQLBigInt mySQLBigInt) {
            super(mySQLBigInt.maximumDisplayWidth(), mySQLBigInt.unSigned(), mySQLBigInt.zerofill());
            if (this.unSigned) {
                this.minValue = BigInteger.ZERO;
                this.maxValue = new BigInteger("2").pow(64).subtract(BigInteger.ONE);
                this.jdbcType = BigInteger.class;
            } else {
                this.minValue = new BigInteger("-2").pow(63);
                this.maxValue = new BigInteger("2").pow(63).subtract(BigInteger.ONE);
                this.jdbcType = Long.class;
            }
        }

        @Override
        public Class<?> getOuterType() {
            return this.jdbcType;
        }

        public static void registerConvertors(ConvertorManager convertorManager) {
            /*----------- 语句转换器 ------------*/
            convertorManager.register(
                new StatementConvertor<BigInteger>(BigInteger.class, Type.class) {
                    @Override
                    public String convert(BigInteger originData) {
                        return originData.toString();
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<Long>(Long.class, Type.class) {
                    @Override
                    public String convert(Long originData) {
                        return originData.toString();
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<Integer>(Integer.class, Type.class) {
                    @Override
                    public String convert(Integer originData) {
                        return originData.toString();
                    }
                }
            );

            /*--------- 结果转换器 当前MySQLBight可能被jdbc转换成两种类型BigInteger 和 Long-----------*/
            convertorManager.register(
                new Convertor<Long, Long>(Long.class, Long.class) {
                    @Override
                    public Long convert(Long originData) {
                        return originData;
                    }
                }
            );
            convertorManager.register(
                new Convertor<BigInteger, BigInteger>(BigInteger.class, BigInteger.class) {
                    @Override
                    public BigInteger convert(BigInteger originData) {
                        return originData;
                    }
                }
            );
            convertorManager.register(
                new Convertor<Long, BigInteger>(Long.class, BigInteger.class) {
                    @Override
                    public BigInteger convert(Long originData) {
                        return new BigInteger(originData.toString());
                    }
                }
            );
            convertorManager.register(
                new Convertor<BigInteger, Long>(BigInteger.class, Long.class) {
                    @Override
                    public Long convert(BigInteger originData) {
                        return originData.longValueExact();
                    }
                }
            );

        }
    }
}
