package zly.rivulet.base.generator.param_manager;

import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.base.describer.param.StaticParam;
import zly.rivulet.base.exception.UnbelievableException;

import java.util.Map;

public class SimpleParamManager implements ParamManager {

    private final Map<String, Object> keyValue;

    public SimpleParamManager(Map<String, Object> keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public Object getParam(ParamDefinition paramDefinition) {
        Param<?> paramDesc = paramDefinition.getOriginDesc();

        if (paramDesc instanceof StaticParam) {
            StaticParam<?> staticParam = (StaticParam<?>) paramDesc;
            return staticParam.getValue();
        } else if (paramDesc instanceof StandardParam) {
            StandardParam<?> standardParam = (StandardParam<?>) paramDesc;
            return keyValue.get(standardParam.getPathKey());
        } else {
            throw UnbelievableException.unknownType();
        }
    }
}
