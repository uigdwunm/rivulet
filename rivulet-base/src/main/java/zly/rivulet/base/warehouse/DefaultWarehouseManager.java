package zly.rivulet.base.warehouse;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.definer.annotations.RivuletDescConfig;
import zly.rivulet.base.definer.annotations.RivuletQueryDesc;
import zly.rivulet.base.definer.annotations.RivuletQueryMapper;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.utils.LoadUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultWarehouseManager implements WarehouseManager {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    private final Map<String, WholeDesc> key_wholeDesc_map = new HashMap<>();

    private final Map<String, Method> key_mapper_map = new HashMap<>();

    public DefaultWarehouseManager(String ... basePackages) {
        for (String basePackage : basePackages) {
            List<Class<?>> classes = LoadUtil.scan(basePackage);
            for (Class<?> clazz : classes) {
                this.putInStorage(clazz);
            }
        }
    }

    /**
     * Description 入库
     *
     * @author zhaolaiyuan
     * Date 2022/7/23 11:49
     **/
    private void putInStorage(Class<?> clazz) {
        RivuletDescConfig rivuletDescConfig = clazz.getAnnotation(RivuletDescConfig.class);
        if (rivuletDescConfig != null) {
            // 是配置类
            try {
                Object o = clazz.newInstance();
                for (Method method : clazz.getMethods()) {
                    RivuletQueryDesc rivuletQueryDesc = method.getAnnotation(RivuletQueryDesc.class);
                    if (rivuletQueryDesc != null) {
                        // 是配置类
                        String key = rivuletQueryDesc.value();
                        WholeDesc wholeDesc = (WholeDesc) method.invoke(o);
                        wholeDesc.setKey(key);
                        key_wholeDesc_map.put(key, wholeDesc);
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            for (Method method : clazz.getMethods()) {
                RivuletQueryMapper rivuletQueryMapper = method.getAnnotation(RivuletQueryMapper.class);
                if (rivuletQueryMapper != null) {
                    key_mapper_map.put(rivuletQueryMapper.value(), method);
                }
            }
        }
    }

    @Override
    public WholeDesc getWholeDesc(String key) {
        return this.key_wholeDesc_map.get(key);
    }

    @Override
    public Method getMapperMethod(String key) {
        return this.key_mapper_map.get(key);
    }

    @Override
    public void register(Method method, RivuletManager rivuletManager) {
        Class<?> clazz = method.getDeclaringClass();
        this.beans.put(clazz, this.createProxyMapper(clazz, rivuletManager));
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
