package zly.rivulet.mysql.definer;

import zly.rivulet.base.definer.outerType.OriginOuterType;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;

import java.lang.reflect.Field;

public final class MySQLFieldMeta extends SQLFieldMeta {

    private final String fieldName;

    private final Class<?> fieldType;

    private final String columnName;

    private final OriginOuterType originOuterType;

    private final String comment;

    private final String defaultValue;

    private final Field field;

    public MySQLFieldMeta(Field field, String columnName, OriginOuterType originOuterType, String comment, String defaultValue) {
        this.fieldName = field.getName();
        this.fieldType = field.getDeclaringClass();
        this.columnName = columnName;
        this.originOuterType = originOuterType;
        this.comment = comment;
        this.defaultValue = defaultValue;
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getComment() {
        return comment;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public OriginOuterType getOriginOuterType() {
        return this.originOuterType;
    }

    @Override
    public String getOriginName() {
        return this.columnName;
    }

    @Override
    public Field getField() {
        return field;
    }
}
