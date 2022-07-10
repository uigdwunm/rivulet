package zly.rivulet.base.runparser.param_manager;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.param.ParamDefinitionSQL;

import java.util.Map;
import java.util.function.Function;

public class ProxyParamManager implements ParamManager {
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
    private final Map<ParamDefinitionSQL, Object> cache;

    private final Map<ParamDefinitionSQL, Function<Object[], Object>> paramCreatorMap;

    public ProxyParamManager(Object[] originParam, Map<ParamDefinitionSQL, Function<Object[], Object>> paramCreatorMap, Map<ParamDefinitionSQL, Object> staticParamMap) {
        this.originParam = originParam;
        this.paramCreatorMap = paramCreatorMap;
        this.cache = staticParamMap;
    }

    @Override
    public Object getParam(ParamDefinitionSQL paramDefinition) {
        Object param = cache.get(paramDefinition);
        if (param != null) {
            return param;
        }

        Function<Object[], Object> creator = paramCreatorMap.get(paramDefinition);
        param = creator.apply(this.originParam);
        cache.put(paramDefinition, param);

        return param;
    }

    @Override
    public String getStatement(ParamDefinitionSQL paramDefinition) {
        Object param = this.getParam(paramDefinition);
        Convertor<Object, ?> convertor = (Convertor<Object, ?>) paramDefinition.getConvertor();
        return convertor.convertToStatement(param);
    }
}
