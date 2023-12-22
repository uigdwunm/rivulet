package zly.rivulet.mysql.discriber.meta;

import zly.rivulet.sql.definer.meta.SQLColumnMeta;
import zly.rivulet.sql.definer.meta.SQLQueryMeta;

public class MySQLColumnMeta<C> extends SQLColumnMeta<C> {

    public MySQLColumnMeta(String name, SQLQueryMeta sqlQueryMeta, Class<C> type) {
        super(name, sqlQueryMeta, type);
    }

}
