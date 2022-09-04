package zly.rivulet.base.generator.param_manager.for_model_meta;

import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.definition.param.PathKeyParamReceipt;
import zly.rivulet.base.definition.param.StaticParamReceipt;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.ClassUtils;

import java.util.*;
import java.util.function.Function;

public class ModelBatchParamManager implements ModelMetaParamManager {

    private List<Object> modelParamList;

    private final Map<String, Function<Object, Object>> paramCreatorMap;

    public ModelBatchParamManager(Object[] originParam, Map<String, Function<Object, Object>> paramCreatorMap) {
        this.paramCreatorMap = paramCreatorMap;

        if (originParam.length != 1) {
            // 无法解析参数
            throw ParseException.failParseParam();
        }
        Object firstParam = originParam[0];
        Class<?> firstClass = firstParam.getClass();

        // 这个判断应该放在外面
        if (ClassUtils.isExtend(Collection.class, firstClass)) {
            // 是集合类型
            this.modelParamList = (List<Object>) firstParam;

        } else if (ClassUtils.isArray(firstClass)) {
            // 是数组类型
            Object[] objs = (Object[]) firstParam;
            this.modelParamList = Arrays.asList(objs);
        } else {
            this.modelParamList = Collections.singletonList(firstParam);
        }
    }


    @Override
    public Object getParam(Object model, ParamReceipt paramReceipt) {
        if (paramReceipt instanceof PathKeyParamReceipt) {
            PathKeyParamReceipt pathKeyParamReceipt = (PathKeyParamReceipt) paramReceipt;
            String pathKey = pathKeyParamReceipt.getPathKey();
            Function<Object, Object> creator = paramCreatorMap.get(pathKey);
            return creator.apply(model);
        } else if (paramReceipt instanceof StaticParamReceipt) {
            StaticParamReceipt staticParamReceipt = (StaticParamReceipt) paramReceipt;
            return staticParamReceipt.getParamValue();
        } else {
            throw UnbelievableException.unknownType();
        }
    }

    public List<Object> getModelParamList() {
        return modelParamList;
    }
}