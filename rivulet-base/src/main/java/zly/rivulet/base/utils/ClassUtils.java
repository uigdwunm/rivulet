package zly.rivulet.base.utils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.exception.UnbelievableException;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ClassUtils {

    private static final Map<Class<?>, Class<?>> baseBoxTypeMap = new HashMap<>();

    static {
        baseBoxTypeMap.put(Byte.TYPE, Byte.class);
        baseBoxTypeMap.put(Integer.TYPE, Integer.class);
        baseBoxTypeMap.put(Long.TYPE, Long.class);
        baseBoxTypeMap.put(Float.TYPE, Float.class);
        baseBoxTypeMap.put(Double.TYPE, Double.class);
        baseBoxTypeMap.put(Character.TYPE, Character.class);
        baseBoxTypeMap.put(Boolean.TYPE, Boolean.class);
    }

    public static Class<?> toBoxType(Class<?> fieldType) {
        return baseBoxTypeMap.getOrDefault(fieldType, fieldType);
    }

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

    /**
     * Description 生成动态代理的对象，插入方法执行后的
     *
     * @author zhaolaiyuan
     * Date 2022/10/10 8:35
     **/
    public static Object dynamicProxy(Class<?> c, BiConsumer<Method, Object[]> afterMethodExec) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(c);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                // 执行
                Object result = methodProxy.invokeSuper(o, args);

                // 方法执行后执行
                afterMethodExec.accept(method, args);

                // 返回原值
                return result;
            }
        });
        return enhancer.create();
    }

    public static Type[] getClassGenericTypes(Class<?> clazz) {
        Type t = clazz.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            return  ((ParameterizedType) t).getActualTypeArguments();
        } else {
            throw UnbelievableException.unknownType();
        }
    }

}
