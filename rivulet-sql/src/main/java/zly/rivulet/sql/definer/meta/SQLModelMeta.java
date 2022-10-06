package zly.rivulet.sql.definer.meta;


import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.utils.View;

public abstract class SQLModelMeta implements ModelMeta, QueryFromMeta {

    public abstract String getTableName();

    public abstract View<SQLFieldMeta> getPrimaryFieldMeta();

    public abstract SQLFieldMeta getFieldMetaByFieldName(String fieldName);
}
