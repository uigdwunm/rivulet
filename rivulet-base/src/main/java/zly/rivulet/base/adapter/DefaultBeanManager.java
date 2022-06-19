package zly.rivulet.base.adapter;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.definer.annotations.RivuletDescConfig;
import zly.rivulet.base.definer.annotations.RivuletQueryDesc;
import zly.rivulet.base.definer.annotations.RivuletQueryMapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBeanManager implements BeanManager {

    private Map<Class<?>, Object> beans = new HashMap<>();

    private List<Method> allConfigMethod = new ArrayList<>();

    private List<Method> allProxyMethod = new ArrayList<>();

    @Override
    public void register(Class<?> clazz, RivuletManager rivuletManager) {

        RivuletDescConfig rivuletDescConfig = clazz.getAnnotation(RivuletDescConfig.class);
        if (rivuletDescConfig != null) {
            // 是配置类
            for (Method method : clazz.getMethods()) {
                RivuletQueryDesc rivuletQueryDesc = method.getAnnotation(RivuletQueryDesc.class);
                if (rivuletQueryDesc != null) {
                    // 是配置类
                    allConfigMethod.add(method);
                }
            }
            try {
                Object o = clazz.newInstance();
                beans.put(clazz, o);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            boolean isProxy = false;
            for (Method method : clazz.getMethods()) {
                RivuletQueryMapper rivuletQueryMapper = method.getAnnotation(RivuletQueryMapper.class);
                if (rivuletQueryMapper != null) {
                    // TODO 是代理接口
                    isProxy = true;
                    allProxyMethod.add(method);
                }
            }

            if (isProxy) {
                Object proxyMapper = createProxyMapper(clazz, rivuletManager);
                beans.put(clazz, proxyMapper);
            }

        }

    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    @Override
    public List<Method> getAllConfigMethod() {
        return this.allConfigMethod;
    }

    @Override
    public List<Method> getAllProxyMethod() {
        return this.allProxyMethod;
    }


    public Object createProxyMapper(Class<?> clazz, RivuletManager rivuletManager) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                // 执行
                RivuletQueryMapper rivuletQueryMapper = method.getAnnotation(RivuletQueryMapper.class);
                if (rivuletQueryMapper != null) {
                    return rivuletManager.exec(method, args);
                } else {
                    return methodProxy.invokeSuper(o, args);
                }
            }
        });
        return enhancer.create();
    }
}
