package vkllyr.jpaminus.parser.preParse;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import vkllyr.jpaminus.JPAMinusManager;
import vkllyr.jpaminus.common.ThreadLocalUtils;
import vkllyr.jpaminus.model.TField;
import vkllyr.jpaminus.model.TFieldAndArgs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DOProxy implements MethodInterceptor {

    private final JPAMinusManager manager;

    // 记录所有方法名称映射的到的字段
    private Map<Class<?>, Map<String, TField>> classMethodNameTFieldMap = new HashMap<>();

    public DOProxy(JPAMinusManager manager) {
        this.manager = manager;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (ifGet(method) || ifSet(method, args)) {
            // TODO do something
        }
        return methodProxy.invokeSuper(o, args);
    }

    private boolean ifGet(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith("get")) {
            TField tField = getOrInitField(method.getDeclaringClass(), methodName.substring(2));
            TFieldAndArgs tFieldAndArgs = new TFieldAndArgs(tField, null);
            // 写到threadLocal上
            ThreadLocalUtils.operationSet(tFieldAndArgs);
            return true;
        } else if (methodName.startsWith("is")) {
            // 对is方法的处理
            TField tField = getOrInitField(method.getDeclaringClass(), methodName.substring(1));
            TFieldAndArgs tFieldAndArgs = new TFieldAndArgs(tField, null);
            // 写到threadLocal上
            ThreadLocalUtils.operationSet(tFieldAndArgs);
            return true;
        }
        return false;
    }

    private boolean ifSet(Method method, Object[] args) {
        String methodName = method.getName();
        if (methodName.startsWith("set")) {
            TField tField = getOrInitField(method.getDeclaringClass(), methodName.substring(2));
            // 写到threadLocal上
            TFieldAndArgs tFieldAndArgs = new TFieldAndArgs(tField, null);
            ThreadLocalUtils.operationSet(tFieldAndArgs);
            return true;
        }
        // TODO 对is方法的处理
        return false;
    }

    /**
     * Description
     *
     * @param methodFieldName 从方法名上剥离出来的属性名，就是大写开头的
     * @author zhaolaiyuan
     * Date 2021/9/12 10:27
     **/
    private TField getOrInitField(Class<?> clazz, String methodFieldName) {
        Map<String, TField> methodNameTFieldMap = classMethodNameTFieldMap.get(clazz);
        if (methodFieldName == null) {
            Field[] fields = clazz.getFields();
            if (fields.length > 0) {
                methodNameTFieldMap = new HashMap<>(fields.length);
            } else {
                methodNameTFieldMap = Collections.emptyMap();
            }
            for (Field field : fields) {
                methodNameTFieldMap.put(field.getName(), new TField(field));
            }

            classMethodNameTFieldMap.put(clazz, methodNameTFieldMap);
        }

        return methodNameTFieldMap.get(methodFieldName);
    }


}
