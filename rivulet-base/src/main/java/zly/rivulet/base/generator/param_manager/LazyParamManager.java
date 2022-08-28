package zly.rivulet.base.generator.param_manager;

import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StaticParam;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LazyParamManager implements ParamManager {
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
    private final Map<ParamReceipt, Object> cache = new HashMap<>();

    private final Map<ParamReceipt, Function<Object[], Object>> paramCreatorMap;

    public LazyParamManager(Object[] originParam, Map<ParamReceipt, Function<Object[], Object>> paramCreatorMap) {
        this.originParam = originParam;
        this.paramCreatorMap = paramCreatorMap;
    }

    @Override
    public Object getParam(ParamReceipt paramReceipt) {
        Object param = cache.get(paramReceipt);
        if (param != null) {
            return param;
        }
        Param<?> paramDesc = paramReceipt.getOriginDesc();
        if (paramDesc instanceof StaticParam) {
            StaticParam<?> staticParam = (StaticParam<?>) paramDesc;
            param = staticParam.getValue();
        } else {
            Function<Object[], Object> creator = paramCreatorMap.get(paramReceipt);
            param = creator.apply(this.originParam);
        }

        cache.put(paramReceipt, param);

        return param;
    }

}
