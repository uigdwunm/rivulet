package zly.rivulet.base.assembly_line.param_manager;

import zly.rivulet.base.definition.param.ParamDefinition;
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
    private final Map<ParamDefinition, Object> cache = new HashMap<>();

    private final Map<ParamDefinition, Function<Object[], Object>> paramCreatorMap;

    public LazyParamManager(Object[] originParam, Map<ParamDefinition, Function<Object[], Object>> paramCreatorMap) {
        this.originParam = originParam;
        this.paramCreatorMap = paramCreatorMap;
    }

    @Override
    public Object getParam(ParamDefinition paramDefinition) {
        Object param = cache.get(paramDefinition);
        if (param != null) {
            return param;
        }
        Param<?> paramDesc = paramDefinition.getOriginDesc();
        if (paramDesc instanceof StaticParam) {
            StaticParam<?> staticParam = (StaticParam<?>) paramDesc;
            param = staticParam.getValue();
        } else {
            Function<Object[], Object> creator = paramCreatorMap.get(paramDefinition);
            param = creator.apply(this.originParam);
        }

        cache.put(paramDefinition, param);

        return param;
    }

}
