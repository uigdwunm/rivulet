package zly.rivulet.mysql.model.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudentVO {

    private String name;

    private boolean gender;

    private String studyTarget;

    private LocalDateTime studyTime;

    private LocalDate birthday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getStudyTarget() {
        return studyTarget;
    }

    public void setStudyTarget(String studyTarget) {
        this.studyTarget = studyTarget;
    }

    public LocalDateTime getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(LocalDateTime studyTime) {
        this.studyTime = studyTime;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "StudentVO{" +
            "name='" + name + '\'' +
            ", gender=" + gender +
            ", studyTarget='" + studyTarget + '\'' +
            ", studyTime=" + studyTime +
            ", birthday=" + birthday +
            '}';
    }
}
