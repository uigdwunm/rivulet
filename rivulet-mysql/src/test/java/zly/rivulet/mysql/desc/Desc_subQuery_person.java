package zly.rivulet.mysql.desc;

import zly.rivulet.mysql.discriber.meta.MySQLColumnMeta;
import zly.rivulet.sql.describer.meta.SQLSubQueryMeta;
import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;

public class Desc_subQuery_person extends SQLSubQueryMeta {
    public static final String TABLE_NAME = "t_person";

    public final MySQLColumnMeta<Long> id = new MySQLColumnMeta<>("id", this, Long.class);

    public final MySQLColumnMeta<String> name = new MySQLColumnMeta<>("name", this, String.class);

    public Desc_subQuery_person(SQLQueryMetaDesc<?> sqlSubQueryMetaDesc) {
        super(sqlSubQueryMetaDesc);
    }

    @Override
    public String name() {
        return TABLE_NAME;
    }
}
