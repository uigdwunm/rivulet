package zly.rivulet.mysql.example.model;

import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLBigInt;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLChar;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SqlTable;

@SqlTable("t_student")
public class Student {

    @SqlColumn("id")
    @MySQLInt
    @Comment("主键自增id")
    private int id;

    @SqlColumn("userId")
    @MySQLBigInt
    @Comment("对应user表的id")
    private long userId;

    @SqlColumn("code")
    @MySQLChar(length = 10)
    @Comment("学号")
    private String code;
}
