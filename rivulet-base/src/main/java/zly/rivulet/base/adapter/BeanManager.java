package zly.rivulet.base.adapter;

import zly.rivulet.base.RivuletManager;

import java.lang.reflect.Method;
import java.util.List;

public interface BeanManager {

    void register(Class<?> clazz, RivuletManager rivuletManager);

    <T> T getBean(Class<T> clazz);


    List<Method> getAllConfigMethod();

    List<Method> getAllProxyMethod();

}
