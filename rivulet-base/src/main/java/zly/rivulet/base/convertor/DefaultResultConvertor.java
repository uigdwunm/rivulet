package zly.rivulet.base.convertor;

import java.math.BigInteger;

public class DefaultResultConvertor {
    public static void registerDefault(ConvertorManager convertorManager) {
        registerBigIntegerConvertor(convertorManager);

    }

    private static void registerBigIntegerConvertor(ConvertorManager convertorManager) {
        convertorManager.register(
            new Convertor<BigInteger, BigInteger>() {
                @Override
                public BigInteger convert(BigInteger originData) {
                    return originData;
                }
            }
        );
    }

    private static void registerLongConvertor(ConvertorManager convertorManager) {
        convertorManager.register(
            new Convertor<Long, Long>() {
                @Override
                public Long convert(Long originData) {
                    return originData;
                }
            }
        );
    }
}
