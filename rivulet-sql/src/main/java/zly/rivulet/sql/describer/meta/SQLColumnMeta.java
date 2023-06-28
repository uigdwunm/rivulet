package zly.rivulet.sql.describer.meta;

import zly.rivulet.base.describer.SingleValueElementDesc;

public class SQLColumnMeta implements SingleValueElementDesc {

    protected final SQLTableMeta sqlTableMeta;

    public SQLColumnMeta(SQLTableMeta sqlTableMeta) {
        this.sqlTableMeta = sqlTableMeta;
    }
}
