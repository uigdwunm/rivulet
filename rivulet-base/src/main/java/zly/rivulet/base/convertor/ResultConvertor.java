package zly.rivulet.base.convertor;

import zly.rivulet.base.utils.ClassUtils;

import java.lang.reflect.Type;

public abstract class ResultConvertor<O, T> extends Convertor<O, T> {

    public ResultConvertor() {
        Type[] classGenericTypes = ClassUtils.getClassGenericTypes(this.getClass());
        super.originType = (Class<O>) classGenericTypes[0];
        super.targetType = (Class<T>) classGenericTypes[1];
    }

    public ResultConvertor(Class<O> originType, Class<T> targetType) {
        super(originType, targetType);
    }

}
