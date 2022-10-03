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

    private final boolean isPrimary;

    public MySQLFieldMeta(Field field, String columnName, OriginOuterType originOuterType, String comment, String defaultValue, boolean isPrimary) {
        this.fieldName = field.getName();
        this.fieldType = field.getDeclaringClass();
        this.columnName = columnName;
        this.originOuterType = originOuterType;
        this.comment = comment;
        this.defaultValue = defaultValue;
        this.field = field;
        this.isPrimary = isPrimary;

        // 字段权限放开，方便操作
        field.setAccessible(true);
    }

    @Override
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

    public boolean isPrimary() {
        return isPrimary;
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
