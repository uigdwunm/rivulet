package zly.rivulet.base.convertor;

import zly.rivulet.base.utils.ClassUtils;

import java.lang.reflect.Type;

public abstract class Convertor<O, T> {
    private final Class<O> originType;

    private final Class<T> targetType;

    public Convertor() {
        Type[] classGenericTypes = ClassUtils.getClassGenericTypes(this.getClass());
        this.originType = (Class<O>) classGenericTypes[0];
        this.targetType = (Class<T>) classGenericTypes[1];
    }

    public Convertor(Class<O> originType, Class<T> targetType) {
        this.originType = originType;
        this.targetType = targetType;
    }

    public abstract T convert(O originData);

    public boolean checkJavaType(Object innerValue) {
        return ClassUtils.isExtend(originType, innerValue.getClass());
    }

    public Class<O> getOriginType() {
        return originType;
    }

    public Class<T> getTargetType() {
        return targetType;
    }
}
