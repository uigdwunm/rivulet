package zly.rivulet.mysql.example.enums;

import zly.rivulet.base.support.TEnum;

public enum UserType implements TEnum<Integer> {
    VISITOR(0, "游客"),
    COMMON(1, "普通用户"),
    VIP(2, "vip");

    private final Integer id;
    private final String desc;

    UserType(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }


    public UserType of(Integer id) {
        for (UserType t : values()) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "UserType{" +
            "id=" + id +
            ", desc='" + desc + '\'' +
            '}';
    }
}
