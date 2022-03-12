package zly.rivulet.mysql.definer;

import zly.rivulet.base.definer.ModelMeta;

import java.util.List;

public class MySQLModelMeta implements ModelMeta {

    private Class<?> modelClass;

    private List<MySQLFieldMeta> fieldMetaList;

    @Override
    public Class<?> getModelClass() {
        return this.modelClass;
    }
}
