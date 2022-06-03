package zly.rivulet.base.exception;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ModelDefineException extends RuntimeException {

    public ModelDefineException(String msg) {
        super(msg);
    }

    public static ModelDefineException multiMainFromMode() {
        return new ModelDefineException("连表查询只有有一个查询主体");
    }

    public static ModelDefineException multiColumnType(Class<?> clazz, Field field) {
        return new ModelDefineException("每个mysql字段只能有一个类型");
    }

    public static ModelDefineException fieldMappingMustGetMethod(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        return new ModelDefineException("字段映射只能解析getter方法");
    }
}
