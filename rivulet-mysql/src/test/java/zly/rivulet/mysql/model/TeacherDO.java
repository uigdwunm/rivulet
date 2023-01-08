package zly.rivulet.mysql.model;

import zly.rivulet.mysql.definer.annotations.type.date.MySQLDatetime;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLBigInt;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLChar;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SqlTable;

import java.time.LocalDateTime;

@SqlTable("t_teacher")
public class TeacherDO {

    @SqlColumn
    @MySQLInt
    private int id;

    @SqlColumn
    @MySQLChar(length = 6)
    private String code;

    @SqlColumn("person_id")
    @MySQLBigInt
    private long personId;

    @SqlColumn("create_time")
    @MySQLDatetime
    private LocalDateTime createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
