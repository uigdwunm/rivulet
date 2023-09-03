package zly.rivulet.base.convertor;

import zly.rivulet.base.utils.ClassUtils;

import java.lang.reflect.Type;

public abstract class ResultConvertor<O, T> extends Convertor<O, T> {

    public static final ResultConvertor<Object, Object> SELF_CONVERTOR = new ResultConvertor<Object, Object>() {
        @Override
        public Object convert(Object originData) {
            return originData;
        }
    };

    @SuppressWarnings("unchecked")
    public ResultConvertor() {
        Type[] classGenericTypes = ClassUtils.getClassGenericTypes(this.getClass());
        super.originType = (Class<O>) classGenericTypes[0];
        super.targetType = (Class<T>) classGenericTypes[1];
    }

    public ResultConvertor(Class<O> originType, Class<T> targetType) {
        super(originType, targetType);
    }

}
