package zly.rivulet.base.definer;

import zly.rivulet.base.convertor.ConvertorManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Description 将DO类解析成对象
 *
 * @author zhaolaiyuan
 * Date 2022/3/12 10:39
 **/
public abstract class Definer {

    private final Map<Class<?>, ModelMeta> classModelMetaMap = new HashMap<>();

    private final ConvertorManager convertorManager;

    protected Definer(ConvertorManager convertorManager) {
        this.convertorManager = convertorManager;
        initTypeConvertor(this.convertorManager);
    }

    public ModelMeta getModelMeta(Class<?> clazz) {
        ModelMeta modelMeta = classModelMetaMap.get(clazz);
        if (modelMeta != null) {
            return modelMeta;
        }
        modelMeta = this.parse(clazz);
        this.classModelMetaMap.put(clazz, modelMeta);
        return modelMeta;
    }

    protected abstract ModelMeta parse(Class<?> clazz);

    protected abstract void initTypeConvertor(ConvertorManager convertorManager);

}
