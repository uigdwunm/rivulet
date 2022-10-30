package zly.rivulet.base.generator.param_manager.for_model_meta;

import zly.rivulet.base.definition.param.PathKeyParamReceipt;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;

import java.util.Map;
import java.util.function.Function;

public class ModelMetaParamManager implements CommonParamManager {

    private final Map<String, Function<Object, Object>> modelMetaParamParserMap;

    private final Object model;

    public ModelMetaParamManager(Map<String, Function<Object, Object>> modelMetaParamParser, Object model) {
        this.modelMetaParamParserMap = modelMetaParamParser;
        this.model = model;
    }

    @Override
    public Object getParam(PathKeyParamReceipt pathKeyParamReceipt) {
        String pathKey = pathKeyParamReceipt.getPathKey();
        Function<Object, Object> modelMetaParamParser = modelMetaParamParserMap.get(pathKey);
        return modelMetaParamParser.apply(model);
    }

}
