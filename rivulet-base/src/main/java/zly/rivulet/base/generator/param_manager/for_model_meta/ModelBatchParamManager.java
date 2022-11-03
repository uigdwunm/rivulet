package zly.rivulet.base.generator.param_manager.for_model_meta;

import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ModelBatchParamManager implements ParamManager {

    private final List<Object> modelParamList;

    private final Map<String, Function<Object, Object>> modelMetaParamParserMap;
    public ModelBatchParamManager(List<Object> modelParamList, Map<String, Function<Object, Object>> modelMetaParamParserMap) {
        this.modelParamList = modelParamList;
        this.modelMetaParamParserMap = modelMetaParamParserMap;
    }

    public CommonParamManager createCommonParamManager(Object model) {
        return new ModelMetaParamManager(modelMetaParamParserMap, model);
    }

    public List<Object> getModelParamList() {
        return modelParamList;
    }
}
