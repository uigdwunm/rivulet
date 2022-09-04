package zly.rivulet.base.definition.param;

import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.generator.param_manager.for_model_meta.ModelBatchParamManager;
import zly.rivulet.base.generator.param_manager.for_model_meta.ModelMetaParamManager;
import zly.rivulet.base.utils.View;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModelMetaParamManagerCreatorHelper {
    public static Function<Object[], ModelMetaParamManager> createParamManagerCreator(ModelMeta modelMeta) {
        View<FieldMeta> fieldMetaList = modelMeta.getFieldMetaList();
        Map<String, Function<Object, Object>> paramCreatorMap = new HashMap<>(fieldMetaList.size());
        for (FieldMeta fieldMeta : fieldMetaList) {
            String fieldName = fieldMeta.getFieldName();
            Function<Object, Object> paramCreator = createParamCreator(fieldMeta.getField());
            paramCreatorMap.put(fieldName, paramCreator);
        }

        return originParams -> new ModelBatchParamManager(originParams, paramCreatorMap);
    }

    private static Function<Object, Object> createParamCreator(Field field) {
        return modelParam -> {
            try {
                return field.get(modelParam);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
