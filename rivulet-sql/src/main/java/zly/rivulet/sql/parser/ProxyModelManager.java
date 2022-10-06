package zly.rivulet.sql.parser;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProxyModelManager {
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    private final Map<Class<?>, Object> proxyModelMap = new ConcurrentHashMap<>();

    public Object get(Class<?> clazz) {
        return proxyModelMap.get(clazz);
    }

    public void save(Class<?> clazz, Object proxyModel) {
        proxyModelMap.put(clazz, proxyModel);
    }

    public Object getOrCreateProxyModel(Class<?> clazz) {
        Object proxyModel = this.get(clazz);
        if (proxyModel == null) {
            return proxyModel;
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                // 执行
                Object result = methodProxy.invokeSuper(o, args);

                // 通过方法名解析出字段，获取fieldMeta
                String methodName = method.getName();
                if (!StringUtil.checkGetterMethodName(methodName)) {
                    // 只能解析get开头的方法
                    return result;
                }


                String fieldName = THREAD_LOCAL.get();
                if (fieldName == null) {
                    fieldName = StringUtil.parseGetterMethodNameToFieldName(methodName);
                    THREAD_LOCAL.set(fieldName);
                }
                return result;
            }
        });
        proxyModel = enhancer.create();
        this.save(clazz, proxyModel);
        return proxyModel;
    }

    public String getFieldNameFromThreadLocal() {
        return THREAD_LOCAL.get();
    }
}
