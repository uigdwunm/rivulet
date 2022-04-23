package zly.rivulet.base.utils;

import zly.rivulet.base.describer.field.FieldMapping;

/**
 * Description 回响工具类，获取回响
 *
 * @author zhaolaiyuan
 * Date 2022/3/20 10:38
 **/
public class EchoUtil {

    private final static ThreadLocal<String> ECHO_THREAD_LOCAL = new ThreadLocal<>();

    public static String getEcho(Object fromObject, FieldMapping<Object, Object> fieldMapping) {
        fieldMapping.getMapping(fromObject);
        return ECHO_THREAD_LOCAL.get();
    }

    public static void echo(String value) {
        ECHO_THREAD_LOCAL.set(value);
    }

    public static void clear() {
        ECHO_THREAD_LOCAL.remove();
    }
}
