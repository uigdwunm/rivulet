package zly.rivulet.base.warehouse;

import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.describer.WholeDesc;

import java.lang.reflect.Method;

public interface WarehouseManager {

    void register(Method method, RivuletManager rivuletManager);

    WholeDesc getWholeDesc(String key);

    Method getMapperMethod(String key);
}
