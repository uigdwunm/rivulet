package zly.rivulet.mysql.discriber.meta;

import zly.rivulet.sql.describer.meta.SQLColumnMeta;

public abstract class MySQLColumnMeta<C> implements SQLColumnMeta<C> {

    protected final MySQLTableMeta sqlTableMeta;

    protected final Class<C> type;

    public MySQLColumnMeta(MySQLTableMeta sqlTableMeta, Class<C> type) {
        this.sqlTableMeta = sqlTableMeta;
        this.type = type;
    }
}
