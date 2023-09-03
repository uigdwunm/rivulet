package zly.rivulet.mysql.desc;

import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLBigInt;
import zly.rivulet.sql.describer.meta.SQLColumnMeta;
import zly.rivulet.sql.describer.meta.SQLTableMeta;

public class Desc_person extends SQLTableMeta {
    public static final String TABLE_NAME = "t_person";

    public final SQLColumnMeta<Long> id = new SQLColumnMeta<>(Long.class, this);

    public final SQLColumnMeta<String> name = new SQLColumnMeta<>(String.class, this);

    @Override
    public void primaryKey() {

    }
}
