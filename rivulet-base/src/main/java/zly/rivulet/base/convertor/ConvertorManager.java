package zly.rivulet.base.convertor;

import zly.rivulet.base.utils.TwofoldConcurrentHashMap;

import java.util.concurrent.ConcurrentHashMap;

public class ConvertorManager {

    /**
     * Description 一般用于查询结果转换成java类型时,就是 jdbc的type转换成java模型的type
     *
     * @author zhaolaiyuan
     * Date 2022/12/4 12:40
     **/
    private final TwofoldConcurrentHashMap<Class<?>, Class<?> , Convertor<?, ?>> resultConvertorMap;

    /**
     * Description java类型转换成语句时的convertor
     *
     * @author zhaolaiyuan
     * Date 2022/12/4 12:39
     **/
    private final ConcurrentHashMap<Class<?>, StatementConvertor<?>> statementConvertorMap;

    public ConvertorManager() {
        this.resultConvertorMap = new TwofoldConcurrentHashMap<>();
        this.statementConvertorMap = new ConcurrentHashMap<>();
    }


    public <T1, T2> void register(Convertor<T1, T2> convertor) {
        Class<T2> targetType = convertor.getTargetType();
        resultConvertorMap.put(convertor.getOriginType(), targetType, convertor);
    }

    public <T1> void register(StatementConvertor<T1> statementConvertor) {
        statementConvertorMap.put(statementConvertor.getOriginType(), statementConvertor);
    }

    private static String getKey(Class<?> javaType, Class<?> outerType) {
        return javaType.getName() + '_' + outerType.getName();
    }

    public <T1, T2> Convertor<T1, T2> getResultConvertor(Class<T1> originType, Class<T2> targetType) {
        return (Convertor<T1, T2>) resultConvertorMap.get(originType, targetType);
    }

    /**
     * Description 获取转换成语句的convertor
     *
     * @author zhaolaiyuan
     * Date 2022/12/4 12:04
     **/
    public <T1> StatementConvertor<T1> getStatementConvertor(Class<T1> javaType) {
        return (StatementConvertor<T1>) statementConvertorMap.get(javaType);
    }
}
