package zly.rivulet.base.definition.param;

import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_model_meta.ModelBatchParamManager;
import zly.rivulet.base.generator.param_manager.for_model_meta.ModelMetaParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ParamManagerFactory {

//    public final Map<Blueprint, Map<Method, Function<Object[], ProxyMethodParamManager>>> proxyMethod_paramManagerCreator_map = new ConcurrentHashMap<>();
    public final TwofoldConcurrentHashMap<Blueprint, Method, Function<Object[], CommonParamManager>> proxyMethod_paramManagerCreator_map = new TwofoldConcurrentHashMap<>();

    public final Map<Class<?>, Map<String, Function<Object, Object>>> modelType_modelMetaParamParser_map = new ConcurrentHashMap<>();

    public ParamManager getByModelMeta(ModelMeta modelMeta, Object model) {
        Map<String, Function<Object, Object>> modelMetaParamParser = this.modelType_modelMetaParamParser_map.get(modelMeta.getModelClass());
        if (modelMetaParamParser == null) {
            modelMetaParamParser = CommonParamManagerCreatorHelper.createModelMetaParamParser(modelMeta);
            this.modelType_modelMetaParamParser_map.put(modelMeta.getModelClass(), modelMetaParamParser);
        }
        return new ModelMetaParamManager(modelMetaParamParser, model);
    }

    public ParamManager getBatchByModelMeta(ModelMeta modelMeta, Collection<Object> models) {
        Map<String, Function<Object, Object>> modelMetaParamParser = this.modelType_modelMetaParamParser_map.get(modelMeta.getModelClass());
        if (modelMetaParamParser == null) {
            modelMetaParamParser = CommonParamManagerCreatorHelper.createModelMetaParamParser(modelMeta);
            this.modelType_modelMetaParamParser_map.put(modelMeta.getModelClass(), modelMetaParamParser);
        }
        return new ModelBatchParamManager(models, modelMetaParamParser);
    }

    public Function<Object[], CommonParamManager> registerProxyMethod(Blueprint blueprint, Method method) {
        ParamReceiptManager paramReceiptManager = blueprint.getParamReceiptManager();
        Function<Object[], CommonParamManager> paramManagerCreator = CommonParamManagerCreatorHelper.createParamManagerCreator(method, paramReceiptManager.getAllParamReceiptList());

        proxyMethod_paramManagerCreator_map.put(blueprint, method, paramManagerCreator);
        return paramManagerCreator;
    }

    public ParamManager getByProxyMethod(Blueprint blueprint, Method method, Object[] params) {
        Function<Object[], CommonParamManager> paramManagerCreator = proxyMethod_paramManagerCreator_map.get(blueprint, method);
        if (paramManagerCreator == null) {
            paramManagerCreator = this.registerProxyMethod(blueprint, method);
        }
        return paramManagerCreator.apply(params);
    }

}
