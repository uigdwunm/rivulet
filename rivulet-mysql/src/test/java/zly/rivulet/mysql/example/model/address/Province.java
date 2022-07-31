package zly.rivulet.mysql.example.model.address;

import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SqlTable;

@SqlTable("t_province")
@Comment("省份表")
public class Province {

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
