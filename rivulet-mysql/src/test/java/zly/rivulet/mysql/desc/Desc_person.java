package zly.rivulet.mysql.desc;

import zly.rivulet.mysql.discriber.meta.MySQLColumnMeta;
import zly.rivulet.mysql.discriber.meta.MySQLTableMeta;
import zly.rivulet.sql.describer.meta.SQLColumnMeta;

public class Desc_person extends MySQLTableMeta {
    public static final String TABLE_NAME = "t_person";

    public final MySQLColumnMeta<Long> id = new MySQLColumnMeta<>("id", this, Long.class);

    public final MySQLColumnMeta<String> name = new MySQLColumnMeta<>("name", this, String.class);

    @Override
    public void primaryKey() {

    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String name() {
        return TABLE_NAME;
    }
}
