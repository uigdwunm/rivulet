package zly.rivulet.mysql.model;

import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.base.definer.annotations.PrimaryKey;
import zly.rivulet.mysql.definer.annotations.type.date.MySQLDate;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLBigInt;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLChar;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SQLTable;

import java.time.LocalDateTime;

@SQLTable("t_student")
public class StudentDO {

    @PrimaryKey
    @SqlColumn("id")
    @MySQLInt
    private Integer id;

    @SqlColumn("person_id")
    @MySQLBigInt
    @Comment("对应person表的主键")
    private Long personId;

    @SqlColumn
    @MySQLChar(length = 12)
    @Comment("学生编号")
    private String code;

    @SqlColumn
    @MySQLVarchar(length = 64)
    private String target;

    @SqlColumn("create_time")
    @MySQLDate
    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "StudentDO{" +
            "id=" + id +
            ", personId=" + personId +
            ", code='" + code + '\'' +
            ", target='" + target + '\'' +
            ", createTime=" + createTime +
            '}';
    }
}
