package zly.rivulet.mysql.definer.annotations.type.numeric;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.DefaultResultConvertor;
import zly.rivulet.base.convertor.StatementConvertor;
import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.mysql.definer.outerType.ExactNumericType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.math.BigInteger;

@Retention(RetentionPolicy.RUNTIME)
public @interface MySQLInt {

    int maximumDisplayWidth() default 1;

    BooleanEnum unSigned() default BooleanEnum.DEFAULT;

    BooleanEnum zerofill() default BooleanEnum.DEFAULT;


    class Type extends ExactNumericType {

        private final int minValue;

        private final long maxValue;

        private final Class<?> jdbcType;

        public Type(MySQLInt mySQLInt) {
            super(mySQLInt.maximumDisplayWidth(), mySQLInt.unSigned(), mySQLInt.zerofill());
            if (this.unSigned) {
                this.minValue = 0;
                this.maxValue = 2L * Integer.MAX_VALUE;
                this.jdbcType = Long.class;
            } else {
                this.minValue = Integer.MIN_VALUE;
                this.maxValue = Integer.MAX_VALUE;
                this.jdbcType = Integer.class;
            }
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

        @Override
        public Class<?> getOuterType() {
            return jdbcType;
        }
    }
}
