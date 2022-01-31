package vkllyr.jpaminus.definer.field;

import vkllyr.jpaminus.converter.Converter;
import vkllyr.jpaminus.definer.field.outerMate.OuterMateInfo;

public final class FieldDefinition {

    // java的字段名
    private String javaFiledName;

    // java对应的数据类型
    private Class<?> javaType;

    // 外部的元信息
    private OuterMateInfo outerMate;

    // 外部的字段名
    private String outerColumnName;

    // 转换器
    private Converter<?, ?> converter;
}
