package zly.rivulet.base.utils;

public class ClassUtils {

    public static boolean isExtend(Class<?> fatherClass, Class<?> childClass) {
        return fatherClass.isAssignableFrom(childClass);
    }
}
