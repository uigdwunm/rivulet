package zly.rivulet.base.exception;

import java.lang.reflect.Field;

public class ModelDefineException extends RuntimeException {

    public ModelDefineException(String msg) {
        super(msg);
    }

    public static ModelDefineException multiColumnType(Class<?> clazz, Field field) {
        return new ModelDefineException("每个mysql映射字段只能有一个类型, class=" + clazz.getName() + ", field=" + field.getName());
    }

    public static ModelDefineException loseType(Class<?> clazz, Field field) {
        return new ModelDefineException("映射字段定义必须有一个类型, class=" + clazz.getName() + ", field=" + field.getName());
    }

    public static ModelDefineException noMatchResultConvertor(Class<?> originType, Class<?> targetType) {
        return new ModelDefineException("没找到匹配的结果转换器,fromType=" + originType.getName() + ", targetType=" + targetType.getName());
    }

    public static ModelDefineException noMatchStatementConvertor(Class<?> javaType) {
        return new ModelDefineException("没找到匹配的语句转换器,javaType=" + javaType.getName());
    }
}
