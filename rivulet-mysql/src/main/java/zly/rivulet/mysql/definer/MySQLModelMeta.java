package zly.rivulet.mysql.definer;

import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLModelMeta extends SQLModelMeta {

    private final String tableName;

    private final Class<?> modelClass;

    private final View<SQLFieldMeta> fieldMetaList;

    private final Map<String, SQLFieldMeta> nameMetaMap;

    private final String comment;

    public MySQLModelMeta(String tableName, Class<?> modelClass, View<SQLFieldMeta> fieldMetaList, String comment) {
        this.tableName = tableName;
        this.modelClass = modelClass;
        this.fieldMetaList = fieldMetaList;
        Map<String, SQLFieldMeta> map = new HashMap<>();
        for (SQLFieldMeta mySQLFieldMeta : fieldMetaList) {
            map.put(mySQLFieldMeta.getFieldName(), mySQLFieldMeta);
        }
        this.nameMetaMap = map;

        this.comment = comment;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    public View<SQLFieldMeta> getFieldMetaList() {
        return fieldMetaList;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public MySQLFieldMeta getFieldMeta(String fieldName) {
        return nameMetaMap.get(fieldName);
    }

    @Override
    public Class<?> getModelClass() {
        return this.modelClass;
    }

    @Override
    public Object getProxy() {
        return null;
    }
}
