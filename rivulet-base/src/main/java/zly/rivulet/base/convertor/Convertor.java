package zly.rivulet.base.convertor;

import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class Convertor<O, T> {
    private final Class<O> originType;

    private final Class<T> targetType;

    public Convertor() {
        Type t = this.getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) t).getActualTypeArguments();
            this.originType = (Class<O>) actualTypeArguments[0];
            this.targetType = (Class<T>) actualTypeArguments[1];
        } else {
            throw UnbelievableException.unknownType();
        }
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
