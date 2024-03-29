package zly.rivulet.base.definer;

import zly.rivulet.base.definer.outerType.OriginOuterType;

import java.lang.reflect.Field;

public interface FieldMeta {
    OriginOuterType getOriginOuterType();

    String getFieldName();

    Class<?> getFieldType();

    String getOriginName();

    Field getField();
}
