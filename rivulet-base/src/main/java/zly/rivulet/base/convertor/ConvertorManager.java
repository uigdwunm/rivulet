package zly.rivulet.base.convertor;

import zly.rivulet.base.definer.outerType.OriginOuterType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConvertorManager {

    private final Map<String, Convertor<?, ?>> CONVERTER_MAP = new ConcurrentHashMap<>();


    public synchronized <T1, T2 extends OriginOuterType> void register(Convertor<T1, T2> convertor) {
        String key = getKey(convertor.getJavaType(), convertor.getOriginOuterType());

        CONVERTER_MAP.put(key, convertor);
    }

    public <T1, T2 extends OriginOuterType> Convertor<T1, T2> get(Class<T1> javaType, OriginOuterType outerType) {
        return get(javaType, outerType.getClass());
    }

    public <T1, T2 extends OriginOuterType> Convertor<T1, T2> get(Class<T1> javaType, Class<?> outerTypeClass) {
        String key = getKey(javaType, outerTypeClass);

        return (Convertor<T1, T2>) CONVERTER_MAP.get(key);
    }


    private static String getKey(Class<?> javaType, Class<?> outerType) {
        return javaType.getName() + '_' + outerType.getName();
    }
}
