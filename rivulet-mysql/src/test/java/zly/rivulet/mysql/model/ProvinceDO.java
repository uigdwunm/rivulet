package zly.rivulet.mysql.model;

import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.base.definer.annotations.PrimaryKey;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SQLTable;

@SQLTable("t_province")
public class ProvinceDO {

    @PrimaryKey
    @SqlColumn("code")
    @MySQLInt
    private Integer code;

    @SqlColumn
    @MySQLVarchar(length = 16)
    @Comment("省份名称")
    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
