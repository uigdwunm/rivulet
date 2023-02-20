package zly.rivulet.mysql.model;

import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.base.definer.annotations.PrimaryKey;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.sql.definer.annotations.SQLColumn;
import zly.rivulet.sql.definer.annotations.SQLTable;

@SQLTable("t_city")
public class CityDO {

    @PrimaryKey
    @SQLColumn("code")
    @MySQLInt
    private Integer code;

    @SQLColumn
    @MySQLVarchar(length = 16)
    @Comment("城市名称")
    private String name;

    @SQLColumn("province_code")
    @MySQLInt
    @Comment("所属省份code")
    private Integer provinceCode;

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

    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }
}
