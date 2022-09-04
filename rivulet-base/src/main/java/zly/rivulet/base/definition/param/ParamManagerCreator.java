package zly.rivulet.base.definition.param;

import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_model_meta.ModelMetaParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.ProxyMethodParamManager;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.utils.Constant;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ParamManagerCreator {

    public final Map<String, Function<Object[], ProxyMethodParamManager>> proxyMethodAndKey_paramManagerCreator_map = new ConcurrentHashMap<>();

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

    public void registerProxyMethod(Blueprint blueprint, Method method) {
        ParamReceiptManager paramReceiptManager = blueprint.getParamReceiptManager();
        Function<Object[], ProxyMethodParamManager> paramManagerCreator = ProxyMethodParamManagerCreateorHelper.createParamManagerCreator(method, paramReceiptManager.getAllParamReceiptList());
        String proxyMethodKey = this.getProxyMethodKey(blueprint, method);
        this.proxyMethodAndKey_paramManagerCreator_map.put(proxyMethodKey, paramManagerCreator);
    }

    public ParamManager getByProxyMethod(Blueprint blueprint, Method method, Object[] params) {
        Function<Object[], ProxyMethodParamManager> paramManagerCreator = proxyMethodAndKey_paramManagerCreator_map.get(this.getProxyMethodKey(blueprint, method));
        if (paramManagerCreator == null) {
            this.registerProxyMethod(blueprint, method);
            paramManagerCreator = proxyMethodAndKey_paramManagerCreator_map.get(this.getProxyMethodKey(blueprint, method));
        }
        return paramManagerCreator.apply(params);
    }

    private String getProxyMethodKey(Blueprint blueprint, Method method) {
        return blueprint.getKey() + Constant.UNDERSCORE + method.getName();
    }

}
