package zly.rivulet.base.generator.param_manager.for_proxy_method;

import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.definition.param.PathKeyParamReceipt;
import zly.rivulet.base.definition.param.StaticParamReceipt;
import zly.rivulet.base.exception.UnbelievableException;

import java.util.Map;
import java.util.function.Function;

public class OnceParamManager implements CommonParamManager {
    /**
     * Description 原始参数
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 12:15
     **/
    protected final Object[] originParam;

    protected final Map<String, Function<Object[], Object>> paramCreatorMap;

    public OnceParamManager(Object[] originParam, Map<String, Function<Object[], Object>> paramCreatorMap) {
        this.originParam = originParam;
        this.paramCreatorMap = paramCreatorMap;
    }

    @Override
    public Object getParam(ParamReceipt paramReceipt) {
        if (paramReceipt instanceof PathKeyParamReceipt) {
            PathKeyParamReceipt pathKeyParamReceipt = (PathKeyParamReceipt) paramReceipt;
            String pathKey = pathKeyParamReceipt.getPathKey();
            Function<Object[], Object> creator = paramCreatorMap.get(pathKey);
            return creator.apply(this.originParam);
        } else if (paramReceipt instanceof StaticParamReceipt) {
            StaticParamReceipt staticParamReceipt = (StaticParamReceipt) paramReceipt;
            return staticParamReceipt.getParamValue();
        } else {
            throw UnbelievableException.unknownType();
        }
    }
}
