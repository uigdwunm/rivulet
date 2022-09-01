package zly.rivulet.mysql.definer;

import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definer.meta.SQLModelMeta;

import java.util.HashMap;
import java.util.Map;

public class MySQLModelMeta extends SQLModelMeta {

    private final String tableName;

    private final Class<?> modelClass;

    private final View<MySQLFieldMeta> fieldMetaList;

    private final Map<String, MySQLFieldMeta> nameMetaMap;

    private final String comment;

    public MySQLModelMeta(String tableName, Class<?> modelClass, View<MySQLFieldMeta> fieldMetaList, String comment) {
        this.tableName = tableName;
        this.modelClass = modelClass;
        this.fieldMetaList = fieldMetaList;
        Map<String, MySQLFieldMeta> map = new HashMap<>();
        for (MySQLFieldMeta mySQLFieldMeta : fieldMetaList) {
            map.put(mySQLFieldMeta.getFieldName(), mySQLFieldMeta);
        }
        this.nameMetaMap = map;

        this.comment = comment;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    public View<FieldMeta> getFieldMetaList() {
        return (View) fieldMetaList;
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
