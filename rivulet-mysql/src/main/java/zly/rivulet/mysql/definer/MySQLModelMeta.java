package zly.rivulet.mysql.definer;

import zly.rivulet.sql.definer.meta.SQLModelMeta;

import java.util.List;

public class MySQLModelMeta extends SQLModelMeta {

    private Class<?> modelClass;

    private List<MySQLFieldMeta> fieldMetaList;

    @Override
    public Class<?> getModelClass() {
        return this.modelClass;
    }

    @Override
    public Object getProxy() {
        return null;
    }
}
