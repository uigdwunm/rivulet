package zly.rivulet.mysql.desc;

import zly.rivulet.mysql.discriber.meta.MySQLColumnMeta;
import zly.rivulet.mysql.discriber.meta.MySQLTableMeta;
import zly.rivulet.sql.definer.meta.SQLColumnMeta;

import java.util.Collections;
import java.util.List;

public class Desc_person extends MySQLTableMeta {
    public static final String TABLE_NAME = "t_person";

    public final MySQLColumnMeta<Long> id = new MySQLColumnMeta<>("id", this, Long.class);

    public final MySQLColumnMeta<String> name = new MySQLColumnMeta<>("name", this, String.class);

    @Override
    public List<SQLColumnMeta<?>> primaryKey() {
        return Collections.singletonList(id);
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
