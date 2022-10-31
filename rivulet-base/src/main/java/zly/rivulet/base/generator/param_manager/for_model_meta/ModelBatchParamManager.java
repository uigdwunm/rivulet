package zly.rivulet.base.generator.param_manager.for_model_meta;

import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.definition.param.PathKeyParamReceipt;
import zly.rivulet.base.definition.param.StaticParamReceipt;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.utils.ClassUtils;

import java.util.*;
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
