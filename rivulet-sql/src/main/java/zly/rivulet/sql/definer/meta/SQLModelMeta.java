package zly.rivulet.sql.definer.meta;


import zly.rivulet.base.definer.ModelMeta;

public abstract class SQLModelMeta implements ModelMeta, QueryFromMeta {

    public abstract SQLFieldMeta getFieldMeta(String fieldName);


    public abstract String getTableName();
}
