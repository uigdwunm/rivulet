package zly.rivulet.mysql.example.model;

import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLBigInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.mysql.example.enums.UserType;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SqlTable;

import java.util.Date;

// PropertyDescriptor
@SqlTable("t_user")
public class UserDO {
    @SqlColumn("id")
    @MySQLBigInt
    private long id;

    @SqlColumn
    @Comment("姓名")
    @MySQLVarchar(length = 64)
    private String name;

    @SqlColumn
    @Comment("学号")
    @MySQLVarchar(length = 32)
    private String code;

    @Comment("好朋友")
    private long friendId;

    private int age;

    private boolean isMale;

    private UserType userType;

    private Date birthday;

    private int cityCode;

    private int provinceCode;


    public UserDO() {
    }

    public UserDO(long id, String name, String code, long friendId, int age, boolean isMale, UserType userType, Date birthday, int cityCode, int provinceCode) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.friendId = friendId;
        this.age = age;
        this.isMale = isMale;
        this.userType = userType;
        this.birthday = birthday;
        this.cityCode = cityCode;
        this.provinceCode = provinceCode;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
