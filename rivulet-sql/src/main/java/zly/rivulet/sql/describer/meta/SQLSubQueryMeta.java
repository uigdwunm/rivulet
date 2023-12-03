package zly.rivulet.sql.describer.meta;

import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;

import java.util.List;

public abstract class SQLSubQueryMeta implements SQLQueryMeta {
    protected final SQLQueryMetaDesc<?> sqlSubQueryMetaDesc;

    private List<SQLColumnMeta<?>> sqlColumnMetaList;

    protected SQLSubQueryMeta(SQLQueryMetaDesc<?> sqlSubQueryMetaDesc) {
        this.sqlSubQueryMetaDesc = sqlSubQueryMetaDesc;
    }

    public SQLQueryMetaDesc<?> getSqlSubQueryMetaDesc() {
        return sqlSubQueryMetaDesc;
    }

    @Override
    public List<SQLColumnMeta<?>> getAllColumnMeta() {
        if (this.sqlColumnMetaList == null) {
            this.sqlColumnMetaList = SQLQueryMeta.super.getAllColumnMeta();
        }
        return this.sqlColumnMetaList;
    }
}
