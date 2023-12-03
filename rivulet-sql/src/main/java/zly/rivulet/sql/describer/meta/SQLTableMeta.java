package zly.rivulet.sql.describer.meta;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.parser.SQLAliasManager;

import java.util.List;

public abstract class SQLTableMeta implements SQLQueryMeta, QueryFromMeta {
    private List<SQLColumnMeta<?>> sqlColumnMetaList;

    public abstract void primaryKey();

    public abstract String getTableName();

    @Override
    public Copier copier() {
        SQLTableMeta oneself = this;
        return new Copier() {
            @Override
            public Definition copy() {
                return oneself;
            }
        };
    }

    @Override
    public List<SQLColumnMeta<?>> getAllColumnMeta() {
        if (this.sqlColumnMetaList == null) {
            this.sqlColumnMetaList = SQLQueryMeta.super.getAllColumnMeta();
        }
        return this.sqlColumnMetaList;
    }
}
