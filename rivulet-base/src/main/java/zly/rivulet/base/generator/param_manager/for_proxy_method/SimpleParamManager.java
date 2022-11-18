package zly.rivulet.base.generator.param_manager.for_proxy_method;

import java.util.Map;

public class SimpleParamManager implements CommonParamManager {

    private final Map<String, Object> keyValue;

    public SimpleParamManager(Map<String, Object> keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public Object getParam(String pathKey) {
        return keyValue.get(pathKey);
    }

}
