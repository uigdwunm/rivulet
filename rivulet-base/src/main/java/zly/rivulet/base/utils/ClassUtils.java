package zly.rivulet.base.utils;

public class ClassUtils {

    public static boolean isExtend(Class<?> fatherClass, Class<?> childClass) {
        return fatherClass.isAssignableFrom(childClass);
    }

    public static boolean isArray(Class<?> clazz) {
        return clazz.isArray();
    }

    public static Object newInstance(Class<?> clazz) {
        try {
            // TODO 实例化这里注意下
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
