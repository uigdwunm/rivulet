package vkllyr.jpaminus.converter;

import java.util.HashMap;
import java.util.Map;

public class ConverterManager {

    private static final Map<String, Converter<?, ?>> CONVERTER_MAP = new HashMap<>();


    public static synchronized <T1, T2> void regist(Class<T1> javaType, Class<T2> outerType, Converter<T1, T2> converter) {

    }
}
