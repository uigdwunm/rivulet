package zly.rivulet.base.generator.param_manager;

import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.base.describer.param.StaticParam;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.StupidMap;

import java.util.Map;

public class ForTestParamManager implements ParamManager {

    private final Map<String, String> keyValue;

    public ForTestParamManager() {
        this(StupidMap.create(k -> "${" + k + "}"));
    }

    public ForTestParamManager(Map<String, String> keyValue) {
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

    @Override
    public String getStatement(ParamDefinition paramDefinition) {
        return (String) this.getParam(paramDefinition);
    }
}
