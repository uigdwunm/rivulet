package zly.rivulet.base.convertor;

import zly.rivulet.base.definer.outerType.OriginOuterType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConvertorManager {

    private final Map<String, Convertor<?, ?>> CONVERTER_MAP = new ConcurrentHashMap<>();

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
        javaType = BOXCLASS_MAP.getOrDefault(javaType, javaType);
        return javaType.getName() + '_' + outerType.getName();
    }
}
