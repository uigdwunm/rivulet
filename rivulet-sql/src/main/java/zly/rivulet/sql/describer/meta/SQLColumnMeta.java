package zly.rivulet.sql.describer.meta;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;

public abstract class SQLColumnMeta<C> implements SingleValueElementDesc<C> , SingleValueElementDefinition {

    protected final String name;

    protected final SQLQueryMeta sqlQueryMeta;

    protected final Class<C> type;

    protected SQLColumnMeta(String name, SQLQueryMeta sqlQueryMeta, Class<C> type) {
        this.name = name;
        this.sqlQueryMeta = sqlQueryMeta;
        this.type = type;
    }

    @Override
    public Class<C> getTargetType() {
        return this.type;
    }

    public String getName() {
        return name;
    }

    public SQLQueryMeta getSqlQueryMeta() {
        return sqlQueryMeta;
    }
}
