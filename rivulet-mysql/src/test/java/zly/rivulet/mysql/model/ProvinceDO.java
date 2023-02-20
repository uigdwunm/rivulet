package zly.rivulet.mysql.model;

import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.base.definer.annotations.PrimaryKey;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.sql.definer.annotations.SQLColumn;
import zly.rivulet.sql.definer.annotations.SQLTable;

@SQLTable("t_province")
public class ProvinceDO {

    @PrimaryKey
    @SQLColumn("code")
    @MySQLInt
    private Integer code;

    @SQLColumn
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

    @Override
    public String toString() {
        return "ProvinceDO{" +
            "code=" + code +
            ", name='" + name + '\'' +
            '}';
    }
}
