package zly.rivulet.sql.definer.meta;


import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.sql.definer.meta.QueryFromMeta;

public abstract class SQLModelMeta implements ModelMeta, QueryFromMeta {

    public abstract SQLFieldMeta getFieldMeta(String fieldName);

}
