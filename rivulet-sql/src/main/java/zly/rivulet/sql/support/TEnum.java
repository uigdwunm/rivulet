package zly.rivulet.sql.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface TEnum<E> {
    E getId();

    static <T, E> T of(Class<T> clazz, E id) {
        try {
            Method values = clazz.getDeclaredMethod("values");
            Object result = values.invoke(clazz);
            TEnum<E>[] enums = (TEnum<E>[]) result;
            for (TEnum<E> e : enums) {
                if (e.getId().equals(id)) {
                    return (T) e;
                }
            }
            return null;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException("不是枚举类型");
        }
    }

}
