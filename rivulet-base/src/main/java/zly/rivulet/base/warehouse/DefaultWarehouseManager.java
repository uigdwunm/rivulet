package zly.rivulet.base.warehouse;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definer.annotations.RivuletMapper;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.DescDefineException;
import zly.rivulet.base.utils.LoadUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultWarehouseManager implements WarehouseManager {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    private final Map<String, WholeDesc> key_wholeDesc_map = new HashMap<>();

    private final Map<String, Method> key_mapper_map = new HashMap<>();

    /**
     * 代理方法与设计图的映射
     **/
    protected final Map<Method, Blueprint> mapperMethod_FinalDefinition_Map = new ConcurrentHashMap<>();

    /**
     * 所有key与设计图的映射
     **/
    protected final Map<String, Blueprint> rivuletKey_blueprint_map = new ConcurrentHashMap<>();

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
        try {
            Object o = null;
            for (Method method : clazz.getMethods()) {
                RivuletDesc rivuletDesc ;
                RivuletMapper rivuletQueryMapper;
                if ((rivuletDesc = method.getAnnotation(RivuletDesc.class)) != null) {
                    // 是配置类
                    String key = rivuletDesc.value();
                    if (o == null) {
                        o = clazz.newInstance();
                    }
                    WholeDesc wholeDesc = (WholeDesc) method.invoke(o);
                    wholeDesc.setAnnotation(rivuletDesc);
                    key_wholeDesc_map.put(key, wholeDesc);
                } else if ((rivuletQueryMapper = method.getAnnotation(RivuletMapper.class)) != null) {
                    key_mapper_map.put(rivuletQueryMapper.value(), method);
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WholeDesc getWholeDesc(String key) {
        WholeDesc wholeDesc = this.key_wholeDesc_map.get(key);
        if (wholeDesc == null) {
            throw DescDefineException.noMatchDescKey(key);
        }
        return wholeDesc;
    }

    @Override
    public Map<String, Method> getAllMapperMethod() {
        return this.key_mapper_map;
    }

    @Override
    public Map<String, WholeDesc> getAllConfiguredDesc() {
        return this.key_wholeDesc_map;
    }

    @Override
    public Blueprint getByProxyMethod(Method proxyMethod) {
        return this.mapperMethod_FinalDefinition_Map.get(proxyMethod);
    }

    @Override
    public void putProxyMethodBlueprint(Method proxyMethod, Blueprint blueprint) {
        this.mapperMethod_FinalDefinition_Map.put(proxyMethod, blueprint);
    }

    @Override
    public Blueprint getByDescKey(String descKey) {
        return this.rivuletKey_blueprint_map.get(descKey);
    }

    @Override
    public void putDescKeyBlueprint(String descKey, Blueprint blueprint) {
        this.rivuletKey_blueprint_map.put(descKey, blueprint);
    }

    @Override
    public Collection<Blueprint> getAllBlueprint() {
        return rivuletKey_blueprint_map.values();
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
                    return rivuletManager.getRivulet().exec(method, args);
                } else {
                    return methodProxy.invokeSuper(o, args);
                }
            }
        });
        return enhancer.create();
    }

}
