package zly.rivulet.base.definition.param;

import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_model_meta.ModelMetaParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.ProxyMethodParamManager;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ParamManagerFactory {

//    public final Map<Blueprint, Map<Method, Function<Object[], ProxyMethodParamManager>>> proxyMethod_paramManagerCreator_map = new ConcurrentHashMap<>();
    public final TwofoldConcurrentHashMap<Blueprint, Method, Function<Object[], ProxyMethodParamManager>> proxyMethod_paramManagerCreator_map = new TwofoldConcurrentHashMap<>();

    public final Map<Class<?>, Function<Object[], ModelMetaParamManager>> modelType_paramManagerCreator_map = new ConcurrentHashMap<>();

    public void registerModelMeta(ModelMeta modelMeta) {
        Function<Object[], ModelMetaParamManager> paramManagerCreator = ModelMetaParamManagerCreatorHelper.createParamManagerCreator(modelMeta);
        // TODO 遗留任务，尽量保证只有批量的才会使用新的ParamManager

        this.modelType_paramManagerCreator_map.put(modelMeta.getModelClass(), paramManagerCreator);
    }

    public ParamManager getByModelMeta(ModelMeta modelMeta, Object[] params) {
        Function<Object[], ModelMetaParamManager> paramManagerCreator = this.modelType_paramManagerCreator_map.get(modelMeta.getModelClass());
        if (paramManagerCreator == null) {
            this.registerModelMeta(modelMeta);
            paramManagerCreator = this.modelType_paramManagerCreator_map.get(modelMeta.getModelClass());
        }
        return paramManagerCreator.apply(params);
    }

    public Function<Object[], ProxyMethodParamManager> registerProxyMethod(Blueprint blueprint, Method method) {
        ParamReceiptManager paramReceiptManager = blueprint.getParamReceiptManager();
        Function<Object[], ProxyMethodParamManager> paramManagerCreator = ProxyMethodParamManagerCreateorHelper.createParamManagerCreator(method, paramReceiptManager.getAllParamReceiptList());

        proxyMethod_paramManagerCreator_map.put(blueprint, method, paramManagerCreator);
        return paramManagerCreator;
    }

    public ParamManager getByProxyMethod(Blueprint blueprint, Method method, Object[] params) {
        Function<Object[], ProxyMethodParamManager> paramManagerCreator = proxyMethod_paramManagerCreator_map.get(blueprint, method);
        if (paramManagerCreator == null) {
            paramManagerCreator = this.registerProxyMethod(blueprint, method);
        }
        return paramManagerCreator.apply(params);
    }

}
