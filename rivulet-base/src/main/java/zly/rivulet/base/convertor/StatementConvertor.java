package zly.rivulet.base.convertor;

import zly.rivulet.base.utils.ClassUtils;

import java.lang.reflect.Type;

public abstract class StatementConvertor<O> extends Convertor<O, String> {

    public StatementConvertor() {
        Type[] classGenericTypes = ClassUtils.getClassGenericTypes(this.getClass());
        this.originType = (Class<O>) classGenericTypes[0];
        this.targetType = String.class;
    }

    public StatementConvertor(Class<O> originType) {
        super(originType, String.class);
    }

}
