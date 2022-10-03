package zly.rivulet.mysql.definer;

import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLModelMeta extends SQLModelMeta {

    private final String tableName;

    private final Class<?> modelClass;

    /**
     * 主键id(多个时为复合主键，为空时没有设置主键)
     **/
    private final View<SQLFieldMeta> primaryKey;

    private final View<MySQLFieldMeta> fieldMetaList;

    private final Map<String, MySQLFieldMeta> nameMetaMap;

    private final String comment;

    public MySQLModelMeta(String tableName, Class<?> modelClass, View<MySQLFieldMeta> fieldMetaList, String comment) {
        this.tableName = tableName;
        this.modelClass = modelClass;
        this.fieldMetaList = fieldMetaList;
        Map<String, MySQLFieldMeta> map = new HashMap<>();
        List<SQLFieldMeta> primaryKey = new ArrayList<>();
        for (MySQLFieldMeta mySQLFieldMeta : fieldMetaList) {
            map.put(mySQLFieldMeta.getFieldName(), mySQLFieldMeta);
            if (mySQLFieldMeta.isPrimary()) {
                primaryKey.add(mySQLFieldMeta);
            }
        }

        this.primaryKey = View.create(primaryKey);
        this.nameMetaMap = map;

        this.comment = comment;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public View<SQLFieldMeta> getPrimaryFieldMeta() {
        return this.primaryKey;
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
