package zly.rivulet.base.warehouse;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definer.annotations.RivuletDescConfig;
import zly.rivulet.base.definer.annotations.RivuletMapper;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.DescDefineException;
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
                    RivuletDesc rivuletDesc = method.getAnnotation(RivuletDesc.class);
                    if (rivuletDesc != null) {
                        // 是配置类
                        String key = rivuletDesc.value();
                        WholeDesc wholeDesc = (WholeDesc) method.invoke(o);
                        wholeDesc.setAnnotation(rivuletDesc);
                        key_wholeDesc_map.put(key, wholeDesc);
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            for (Method method : clazz.getMethods()) {
                RivuletMapper rivuletQueryMapper = method.getAnnotation(RivuletMapper.class);
                if (rivuletQueryMapper != null) {
                    key_mapper_map.put(rivuletQueryMapper.value(), method);
                }
            }
        }
    }

    @Override
    public WholeDesc getWholeDesc(String key) {
        WholeDesc wholeDesc = this.key_wholeDesc_map.get(key);
        if (wholeDesc == null) {
            throw DescDefineException.noMatchDescKey();
        }
        return wholeDesc;
    }

    @Override
    public Map<String, Method> getAllMapperMethod() {
        return this.key_mapper_map;
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
                RivuletMapper rivuletMapper = method.getAnnotation(RivuletMapper.class);
                if (rivuletMapper != null) {
                    return rivuletManager.exec(method, args);
                } else {
                    return methodProxy.invokeSuper(o, args);
                }
            }
        });
        return enhancer.create();
    }

}
