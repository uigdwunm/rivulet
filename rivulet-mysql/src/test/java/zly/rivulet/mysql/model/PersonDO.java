package zly.rivulet.mysql.model;

import zly.rivulet.base.definer.annotations.PrimaryKey;
import zly.rivulet.mysql.definer.annotations.type.date.MySQLDate;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLBigInt;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLTinyInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SqlTable;

import java.time.LocalDate;

@SqlTable("t_person")
public class PersonDO {

    @PrimaryKey
    @SqlColumn("id")
    @MySQLBigInt
    private Long id;

    @SqlColumn("birthday")
    @MySQLDate
    private LocalDate birthday;

    @SqlColumn
    @MySQLVarchar(length = 64)
    private String name;

    @SqlColumn
    @MySQLTinyInt
    private boolean gender;


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

    @Override
    public String toString() {
        return "PersonDO{" +
            "id=" + id +
            ", birthday=" + birthday +
            ", name='" + name + '\'' +
            ", gender=" + gender +
            '}';
    }
}
