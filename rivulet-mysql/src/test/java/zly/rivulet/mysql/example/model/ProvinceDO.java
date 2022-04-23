package zly.rivulet.mysql.example.model;

import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SqlTable;

@SqlTable
@Comment("省份表")
public class ProvinceDO {

    @Comment("简称")
    @SqlColumn("shortName")
    @MySQLVarchar(length = 8)
    private String shortName;

    @Comment("全称")
    @SqlColumn
    @MySQLVarchar(length = 16)
    private String name;

    @Comment("省份编码")
    @SqlColumn
    @MySQLInt
    private int code;

}
