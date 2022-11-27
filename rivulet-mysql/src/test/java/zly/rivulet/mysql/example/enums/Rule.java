package zly.rivulet.mysql.example.enums;

import zly.rivulet.base.support.TEnum;

public enum Rule implements TEnum<Integer> {

    STUDENT(1, "学生"),
    TEACHER(2, "教师"),
    ;

    private final int id;
    private final String desc;

    Rule(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
