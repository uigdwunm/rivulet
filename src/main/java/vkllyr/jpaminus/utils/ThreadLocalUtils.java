package vkllyr.jpaminus.utils;

import vkllyr.jpaminus.model.TFieldAndArgs;

import java.util.ConcurrentModificationException;

public class ThreadLocalUtils {

    private static final ThreadLocal<Object> OPERATION_RECORD = new ThreadLocal<>();

    public static <T> T operationGet() {
        Object o = OPERATION_RECORD.get();
        if (o == null) {
            return null;
        }
        try {
            return (T) o;
        } catch (Exception e) {
            // 字段操作修改相关操作必须是单线程顺序的。
            throw new ConcurrentModificationException();
        }
    }

    public static void operationSet(Object value) {
        OPERATION_RECORD.set(value);
    }
}
