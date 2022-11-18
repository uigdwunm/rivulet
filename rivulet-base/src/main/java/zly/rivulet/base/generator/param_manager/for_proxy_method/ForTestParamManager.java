package zly.rivulet.base.generator.param_manager.for_proxy_method;

import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.utils.StupidMap;

import java.util.Map;

public class ForTestParamManager extends SimpleParamManager {

    public ForTestParamManager() {
        this(StupidMap.create(k -> "${" + k + "}"));
    }

    public ForTestParamManager(Map<String, String> keyValue) {
        super((Map) keyValue);
    }

    @Override
    public String getStatement(ParamReceipt paramReceipt) {
        return (String) this.getParam(paramReceipt);
    }
}
