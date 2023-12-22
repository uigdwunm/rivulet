package zly.rivulet.sql.definer.meta;

import java.util.List;

public abstract class SQLTableMeta implements SQLQueryMeta, QueryFromMeta {
    private List<SQLColumnMeta<?>> sqlColumnMetaList;

    /**
     * 主键
     **/
    public abstract List<SQLColumnMeta<?>> primaryKey();

    public abstract String getTableName();

    @Override
    public Copier copier() {
        return new ThisCopier<>(this);
    }

    @Override
    public List<SQLColumnMeta<?>> getAllColumnMeta() {
        if (this.sqlColumnMetaList == null) {
            this.sqlColumnMetaList = SQLQueryMeta.super.getAllColumnMeta();
        }
        return this.sqlColumnMetaList;
    }
}
