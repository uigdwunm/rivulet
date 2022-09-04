package zly.rivulet.base.generator.param_manager.for_proxy_method;

import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.definition.param.PathKeyParamReceipt;
import zly.rivulet.base.definition.param.StaticParamReceipt;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.StupidMap;

import java.util.Map;

public class ForTestParamManager implements ProxyMethodParamManager {

    private final Map<String, String> keyValue;

    public ForTestParamManager() {
        this(StupidMap.create(k -> "${" + k + "}"));
    }

    public ForTestParamManager(Map<String, String> keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public Object getParam(ParamReceipt paramReceipt) {
        if (paramReceipt instanceof PathKeyParamReceipt) {
            PathKeyParamReceipt pathKeyParamReceipt = (PathKeyParamReceipt) paramReceipt;
            String pathKey = pathKeyParamReceipt.getPathKey();
            return keyValue.get(pathKey);
        } else if (paramReceipt instanceof StaticParamReceipt) {
            StaticParamReceipt staticParamReceipt = (StaticParamReceipt) paramReceipt;
            return staticParamReceipt.getParamValue();
        } else {
            throw UnbelievableException.unknownType();
        }
    }

    @Override
    public String getStatement(ParamReceipt paramReceipt) {
        return (String) this.getParam(paramReceipt);
    }
}
