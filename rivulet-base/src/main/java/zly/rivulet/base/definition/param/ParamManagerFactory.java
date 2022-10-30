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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ParamManagerFactory {

//    public final Map<Blueprint, Map<Method, Function<Object[], ProxyMethodParamManager>>> proxyMethod_paramManagerCreator_map = new ConcurrentHashMap<>();
    public final TwofoldConcurrentHashMap<Blueprint, Method, Function<Object[], CommonParamManager>> proxyMethod_paramManagerCreator_map = new TwofoldConcurrentHashMap<>();

    public final Map<Class<?>, Function<Object[], CommonParamManager>> modelType_paramManagerCreator_map = new ConcurrentHashMap<>();

    public Function<Object[], CommonParamManager> registerModelMeta(Blueprint blueprint, ModelMeta modelMeta) {
        ParamReceiptManager paramReceiptManager = blueprint.getParamReceiptManager();
        Function<Object[], CommonParamManager> paramManagerCreator = CommonParamManagerCreatorHelper.createParamManagerCreator(modelMeta, paramReceiptManager.getAllParamReceiptList());
        // TODO 遗留任务，尽量保证只有批量的才会使用新的ParamManager

        this.modelType_paramManagerCreator_map.put(modelMeta.getModelClass(), paramManagerCreator);

        return paramManagerCreator;
    }

    public ParamManager getByModelMeta(Blueprint blueprint, ModelMeta modelMeta, Object model) {
        Function<Object[], CommonParamManager> paramManagerCreator = this.modelType_paramManagerCreator_map.get(modelMeta.getModelClass());
        if (paramManagerCreator == null) {
            paramManagerCreator = this.registerModelMeta(blueprint, modelMeta);
        }
        return paramManagerCreator.apply(new Object[]{model});
    }

    public ParamManager getBatchByModelMeta(Blueprint blueprint, ModelMeta modelMeta, Collection<Object> models) {
        Function<Object[], CommonParamManager> paramManagerCreator = this.modelType_paramManagerCreator_map.get(modelMeta.getModelClass());
        if (paramManagerCreator == null) {
            paramManagerCreator = this.registerModelMeta(blueprint, modelMeta);
        }
        return new ModelBatchParamManager()
        return paramManagerCreator.apply(params);
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
