package zly.rivulet.base.generator.param_manager.for_proxy_method;

import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.definition.param.PathKeyParamReceipt;
import zly.rivulet.base.definition.param.StaticParamReceipt;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.generator.param_manager.ParamManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LazyParamManager implements ProxyMethodParamManager {
    /**
     * Description 原始参数
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 12:15
     **/
    private final Object[] originParam;

    /**
     * Description 每次查询都有的，所以不用并发
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 12:16
     **/
    private final Map<String, Object> cache = new HashMap<>();

    private final Map<String, Function<Object[], Object>> paramCreatorMap;

    public LazyParamManager(Object[] originParam, Map<String, Function<Object[], Object>> paramCreatorMap) {
        this.originParam = originParam;
        this.paramCreatorMap = paramCreatorMap;
    }

    @Override
    public Object getParam(ParamReceipt paramReceipt) {
        if (paramReceipt instanceof PathKeyParamReceipt) {
            PathKeyParamReceipt pathKeyParamReceipt = (PathKeyParamReceipt) paramReceipt;
            String pathKey = pathKeyParamReceipt.getPathKey();
            Object param = cache.get(pathKey);
            if (param != null) {
                return param;
            }
            Function<Object[], Object> creator = paramCreatorMap.get(pathKey);
            param = creator.apply(this.originParam);
            cache.put(pathKey, param);
            return param;
        } else if (paramReceipt instanceof StaticParamReceipt) {
            StaticParamReceipt staticParamReceipt = (StaticParamReceipt) paramReceipt;
            return staticParamReceipt.getParamValue();
        } else {
            throw UnbelievableException.unknownType();
        }
    }

}
