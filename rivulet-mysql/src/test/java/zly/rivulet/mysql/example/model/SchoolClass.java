package zly.rivulet.mysql.example.model;

import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SqlTable;

@SqlTable("t_schoolClass")
@Comment("班级表")
public class SchoolClass {

    @SqlColumn("id")
    @MySQLInt
    @Comment("主键自增id")
    private int id;

}
