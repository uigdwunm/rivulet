package zly.rivulet.base.generator.param_manager.for_proxy_method;

import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.definition.param.PathKeyParamReceipt;
import zly.rivulet.base.definition.param.StaticParamReceipt;
import zly.rivulet.base.exception.UnbelievableException;

import java.util.Map;

public class SimpleParamManager implements ProxyMethodParamManager {

    private final Map<String, Object> keyValue;

    public SimpleParamManager(Map<String, Object> keyValue) {
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
}
