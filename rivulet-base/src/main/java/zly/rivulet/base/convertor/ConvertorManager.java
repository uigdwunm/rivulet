package zly.rivulet.base.convertor;

import zly.rivulet.base.definer.outerType.OriginOuterType;
import zly.rivulet.base.definer.outerType.SelfType;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;

import java.util.HashMap;
import java.util.Map;

public class ConvertorManager {

    /**
     * Description 一般用于查询结果转换成java类型时,就是 jdbc的type转换成java模型的type
     *
     * @author zhaolaiyuan
     * Date 2022/12/4 12:40
     **/
    private final TwofoldConcurrentHashMap<Class<?>, Class<?> , Convertor<?, ?>> resultConvertorMap = new TwofoldConcurrentHashMap<>();

    /**
     * Description java类型转换成语句时的convertor
     *
     * @author zhaolaiyuan
     * Date 2022/12/4 12:39
     **/
    private final TwofoldConcurrentHashMap<Class<?>, Class<? extends OriginOuterType> , StatementConvertor<?>> statementConvertorMap = new TwofoldConcurrentHashMap<>();

    private static final Map<Class<?>, Class<?>> BOXCLASS_MAP = new HashMap<>();
    static {
        BOXCLASS_MAP.put(Boolean.TYPE, Boolean.class);
        BOXCLASS_MAP.put(Character.TYPE, Character.class);
        BOXCLASS_MAP.put(Byte.TYPE, Byte.class);
        BOXCLASS_MAP.put(Short.TYPE, Short.class);
        BOXCLASS_MAP.put(Integer.TYPE, Integer.class);
        BOXCLASS_MAP.put(Long.TYPE, Long.class);
        BOXCLASS_MAP.put(Float.TYPE, Float.class);
        BOXCLASS_MAP.put(Double.TYPE, Double.class);
        BOXCLASS_MAP.put(Void.TYPE, Void.class);
    }

    public void init() {
        DefaultResultConvertor.registerDefault(this);
    }

    public <T1, T2> void register(Convertor<T1, T2> convertor) {
        Class<T2> targetType = convertor.getTargetType();
        resultConvertorMap.put(convertor.getOriginType(), targetType, convertor);
    }

    public <T1> void register(StatementConvertor<T1> statementConvertor) {
        statementConvertorMap.put(statementConvertor.getOriginType(), statementConvertor.getOriginOuterType(), statementConvertor);
    }

    private static String getKey(Class<?> javaType, Class<?> outerType) {
        return javaType.getName() + '_' + outerType.getName();
    }

    public <T1, T2> Convertor<T1, T2> getResultConvertor(Class<T1> javaType, Class<T2> targetType) {
        return (Convertor<T1, T2>) resultConvertorMap.get(javaType, targetType);
    }


    /**
     * Description 获取转换成语句的convertor,没有意向类型时用这个
     *
     * @author zhaolaiyuan
     * Date 2022/12/4 12:04
     **/
    public <T1> StatementConvertor<T1> getStatementConvertor(Class<T1> javaType) {
        return this.getStatementConvertor(javaType, SelfType.class);
    }

    /**
     * Description 获取转换成语句的convertor
     *
     * @author zhaolaiyuan
     * Date 2022/12/4 12:04
     **/
    public <T1, T2 extends OriginOuterType> StatementConvertor<T1> getStatementConvertor(Class<T1> javaType, Class<T2> targetType) {
        return (StatementConvertor<T1>) statementConvertorMap.get(javaType, targetType);
    }
}
