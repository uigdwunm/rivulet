package zly.rivulet.sql.definer;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.definer.outerType.OriginOuterType;
import zly.rivulet.sql.definer.meta.SQLModelMeta;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class SqlDefiner implements Definer {

    /**
     * 注解和类型对象的映射
     **/
    protected final Map<Class<?>, Function<Annotation, OriginOuterType>> annotation_TypeCreator_Map = new HashMap<>();

    private final Map<Class<?>, SQLModelMeta> classModelMetaMap = new HashMap<>();

    protected final ConvertorManager convertorManager;

    protected SqlDefiner(ConvertorManager convertorManager) {
        this.convertorManager = convertorManager;
        initTypeConvertor();
    }

    public SQLModelMeta createOrGetModelMeta(Class<?> clazz) {
        if (this.isCglibProxyObj(clazz)) {
            // 判断如果是代理对象，则拿到原class
            clazz = clazz.getSuperclass();
        }
        // 先从缓存拿
        SQLModelMeta modelMeta = classModelMetaMap.get(clazz);
        if (modelMeta != null) {
            return modelMeta;
        }

        // 缓存找不到再解析
        modelMeta = this.parse(clazz);

        // 再存到缓存里
        this.classModelMetaMap.put(clazz, modelMeta);
        // 返回
        return modelMeta;
    }

    private boolean isCglibProxyObj(Class<?> clazz) {
        return clazz.getName().contains("$$");
    }

    protected abstract SQLModelMeta parse(Class<?> clazz);

    protected abstract void initTypeConvertor();

}
