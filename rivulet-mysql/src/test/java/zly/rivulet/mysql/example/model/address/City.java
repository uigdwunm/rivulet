package zly.rivulet.mysql.example.model.address;

import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SqlTable;

@SqlTable("t_city")
public class City {

    @SqlColumn
    @MySQLVarchar(length = 16)
    private String shortName;

    @SqlColumn
    @MySQLVarchar(length = 16)
    private String name;

    @SqlColumn("code")
    @MySQLInt
    private int code;

    @SqlColumn
    @MySQLInt
    private int provinceCode;

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

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
