package zly.rivulet.mysql.definer;

import zly.rivulet.base.definer.outerType.OriginOuterType;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;

public final class MySQLFieldMeta extends SQLFieldMeta {

    private final String fieldName;

    private final Class<?> fieldType;

    private final String columnName;

    private final OriginOuterType originOuterType;

    private final String comment;

    private final String defaultValue;

    public MySQLFieldMeta(String fieldName, Class<?> fieldType, String columnName, OriginOuterType originOuterType, String comment, String defaultValue) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.columnName = columnName;
        this.originOuterType = originOuterType;
        this.comment = comment;
        this.defaultValue = defaultValue;
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
}
