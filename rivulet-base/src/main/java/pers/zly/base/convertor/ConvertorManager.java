package pers.zly.base.convertor;

import java.util.HashMap;
import java.util.Map;

public class ConvertorManager {

    // TODO 是否要换成并发的
    private static final Map<String, Convertor<?, ?>> CONVERTER_MAP = new HashMap<>();


    public static synchronized <T1, T2> void regist(Class<T1> javaType, Class<T2> outerType, Convertor<T1, T2> convertor) {
        String key = getKey(javaType, outerType);

        CONVERTER_MAP.put(key, convertor);
    }

    public static <T1, T2> Convertor<T1, T2> get(Class<T1> javaType, Class<T2> outerType) {
        String key = getKey(javaType, outerType);

        return (Convertor<T1, T2>) CONVERTER_MAP.get(key);
    }


    private static String getKey(Class<?> javaType, Class<?> outerType) {
        return javaType.getName() + ',' + outerType.getName();
    }
}
