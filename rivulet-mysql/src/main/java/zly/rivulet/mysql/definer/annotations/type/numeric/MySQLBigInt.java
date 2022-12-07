package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.StatementConvertor;
import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.mysql.definer.outerType.ExactNumericType;
import zly.rivulet.base.convertor.DefaultResultConvertor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
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

        /**
         * Description 这里是语句转换器(指sql语句传入参数如何转换成语句的一部分)
         *
         * 结果转换器默认值看这里(指查询的结果如果转换为模型中适配的类型):
         * @see DefaultResultConvertor#registerBigIntegerConvertor(ConvertorManager)
         * @see DefaultResultConvertor#registerLongConvertor(ConvertorManager)
         *
         * @author zhaolaiyuan
         * Date 2022/12/6 8:54
         **/
        public static void registerConvertors(ConvertorManager convertorManager) {
            /*----------- 语句转换器 ------------*/
            convertorManager.register(
                new StatementConvertor<BigInteger>(Type.class) {
                    @Override
                    public String convert(BigInteger originData) {
                        return originData.toString();
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<BigDecimal>(Type.class) {
                    @Override
                    public String convert(BigDecimal originData) {
                        return originData.toBigIntegerExact().toString();
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<Long>(Type.class) {
                    @Override
                    public String convert(Long originData) {
                        return originData.toString();
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<Long>(long.class, Type.class) {
                    @Override
                    public String convert(Long originData) {
                        return originData.toString();
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<Integer>(Type.class) {
                    @Override
                    public String convert(Integer originData) {
                        return originData.toString();
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<Integer>(int.class, Type.class) {
                    @Override
                    public String convert(Integer originData) {
                        return originData.toString();
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<Byte>(Type.class) {
                    @Override
                    public String convert(Byte originData) {
                        return originData.toString();
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<Byte>(byte.class, Type.class) {
                    @Override
                    public String convert(Byte originData) {
                        return originData.toString();
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<Boolean>(Type.class) {
                    @Override
                    public String convert(Boolean originData) {
                        return originData ? Constant.ONE_STR : Constant.ZERO_STR;
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<Boolean>(boolean.class, Type.class) {
                    @Override
                    public String convert(Boolean originData) {
                        return originData ? Constant.ONE_STR : Constant.ZERO_STR;
                    }
                }
            );
            convertorManager.register(
                new StatementConvertor<String>(Type.class) {
                    @Override
                    public String convert(String originData) {
                        return originData;
                    }
                }
            );
        }
    }
}
