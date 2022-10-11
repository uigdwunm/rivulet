package zly.rivulet.mysql.example.model.user;

import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLBigInt;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.mysql.example.enums.Rule;
import zly.rivulet.mysql.example.enums.UserType;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SqlTable;

import java.util.Date;

// PropertyDescriptor
@SqlTable("t_user")
public class User {
    @SqlColumn("id")
    @MySQLBigInt
    private long id;

    @SqlColumn
    @Comment("姓名")
    @MySQLVarchar(length = 64)
    private String name;

    @SqlColumn
    @Comment("年龄")
    @MySQLInt
    private int age;

    private boolean isMale;

    private UserType userType;

    private Rule rule;

    private Date birthday;

    @SqlColumn
    @Comment("城市")
    @MySQLInt
    private int cityCode;

    private int provinceCode;


    public User() {
    }

    public User(long id, String name, int age, boolean isMale, UserType userType, Date birthday, int cityCode, int provinceCode) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.isMale = isMale;
        this.userType = userType;
        this.birthday = birthday;
        this.cityCode = cityCode;
        this.provinceCode = provinceCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
