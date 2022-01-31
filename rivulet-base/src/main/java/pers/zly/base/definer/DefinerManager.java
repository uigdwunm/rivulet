package pers.zly.base.definer;

import pers.zly.base.exception.DescDefineException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefinerManager {
    private static final Map<Class<?>, ModelMeta> modelDefinitionMap = new ConcurrentHashMap<>();

    public static ModelMeta getModelDefinition(Class<?> clazz) {
        ModelMeta modelMeta = modelDefinitionMap.get(clazz);
        if (modelMeta == null) {
            throw DescDefineException.unknownFromModel(clazz);
        }
        return modelMeta;
    }
}
