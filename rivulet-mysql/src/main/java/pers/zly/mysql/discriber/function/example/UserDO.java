package pers.zly.mysql.discriber.function.example;

import java.util.Date;

// PropertyDescriptor
public class UserDO {
    private long id;

    private String name;

    private int age;

    private boolean isMale;

    private UserType userType;

    private Date birthday;

    public UserDO(long id, String name, int age, boolean isMale, UserType userType, Date birthday) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.isMale = isMale;
        this.userType = userType;
        this.birthday = birthday;
    }

    public UserDO() {
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
}
