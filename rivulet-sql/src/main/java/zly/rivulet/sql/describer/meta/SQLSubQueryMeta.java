package zly.rivulet.sql.describer.meta;

import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;

public abstract class SQLSubQueryMeta implements SQLQueryMeta {
    protected final SQLQueryMetaDesc<?> sqlSubQueryMetaDesc;

    protected SQLSubQueryMeta(SQLQueryMetaDesc<?> sqlSubQueryMetaDesc) {
        this.sqlSubQueryMetaDesc = sqlSubQueryMetaDesc;
    }

    public SQLQueryMetaDesc<?> getSqlSubQueryMetaDesc() {
        return sqlSubQueryMetaDesc;
    }
}
