package zly.rivulet.base.warehouse;

import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.describer.WholeDesc;

import java.lang.reflect.Method;
import java.util.Map;

public interface WarehouseManager {

    void register(Method method, RivuletManager rivuletManager);

    WholeDesc getWholeDesc(String key);

    Map<String, Method> getAllMapperMethod();
}
