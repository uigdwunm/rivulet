package zly.rivulet.base.generator.param_manager.for_proxy_method;

import zly.rivulet.base.definition.param.PathKeyParamReceipt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LazyParamManager extends OnceParamManager {

    /**
     * Description 每次查询都有的，所以不用并发
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 12:16
     **/
    private final Map<String, Object> cache = new HashMap<>();


    public LazyParamManager(Object[] originParam, Map<String, Function<Object[], Object>> paramCreatorMap) {
        super(originParam, paramCreatorMap);
    }

    @Override
    public Object getParam(PathKeyParamReceipt pathKeyParamReceipt) {
        String pathKey = pathKeyParamReceipt.getPathKey();
        Object param = cache.get(pathKey);
        if (param != null) {
            return param;
        }
        Function<Object[], Object> creator = super.paramCreatorMap.get(pathKey);
        param = creator.apply(super.originParam);
        cache.put(pathKey, param);
        return param;
    }

}
