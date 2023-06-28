package zly.rivulet.mysql.desc;

import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLBigInt;
import zly.rivulet.sql.describer.meta.SQLColumnMeta;
import zly.rivulet.sql.describer.meta.SQLTableMeta;

public class Desc_person extends SQLTableMeta {
    public static final String TABLE_NAME = "t_person";

    public final SQLColumnMeta id = new SQLColumnMeta(this);

    public final SQLColumnMeta name = new SQLColumnMeta(this);

    public MySQLColumnDesc id;

    @Override
    protected void primaryKey() {

    }
}
