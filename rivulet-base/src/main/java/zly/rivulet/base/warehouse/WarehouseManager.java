package zly.rivulet.base.warehouse;

import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public interface WarehouseManager {

    void register(Method method, RivuletManager rivuletManager);

    WholeDesc getWholeDesc(String key);

    Map<String, Method> getAllMapperMethod();

    Map<String, WholeDesc> getAllConfiguredDesc();

    Blueprint getByProxyMethod(Method proxyMethod);

    void putProxyMethodBlueprint(Method proxyMethod, Blueprint blueprint);

    Blueprint getByDescKey(String descKey);

    void putDescKeyBlueprint(String descKey, Blueprint blueprint);

    Collection<Blueprint> getAllBlueprint();

}
