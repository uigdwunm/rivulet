package zly.rivulet.mysql.model;

import zly.rivulet.base.definer.annotations.PrimaryKey;
import zly.rivulet.mysql.definer.annotations.type.date.MySQLDate;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLBigInt;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLTinyInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLJson;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.sql.definer.annotations.SQLColumn;
import zly.rivulet.sql.definer.annotations.SQLTable;

import java.time.LocalDate;

@SQLTable("t_person")
public class PersonDO {

    @PrimaryKey
    @SQLColumn("id")
    @MySQLBigInt
    private Long id;

    @SQLColumn("birthday")
    @MySQLDate
    private LocalDate birthday;

    @SQLColumn
    @MySQLVarchar(length = 64)
    private String name;

    @SQLColumn
    @MySQLTinyInt
    private boolean gender;

    @SQLColumn
    @MySQLJson
    private ProvinceDO extra;


    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public ProvinceDO getExtra() {
        return extra;
    }

    public void setExtra(ProvinceDO extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "PersonDO{" +
            "id=" + id +
            ", birthday=" + birthday +
            ", name='" + name + '\'' +
            ", gender=" + gender +
            ", extra=" + extra +
            '}';
    }
}
