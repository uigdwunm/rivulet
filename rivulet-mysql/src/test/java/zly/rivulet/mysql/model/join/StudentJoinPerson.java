package zly.rivulet.mysql.model.join;

import zly.rivulet.mysql.model.PersonDO;
import zly.rivulet.mysql.model.StudentDO;
import zly.rivulet.sql.describer.condition.JoinCondition;
import zly.rivulet.sql.describer.join.ComplexDescriber;
import zly.rivulet.sql.describer.join.QueryComplexModel;

public class StudentJoinPerson implements QueryComplexModel {

    private PersonDO personDO;

    private StudentDO studentDO;

    @Override
    public ComplexDescriber register() {
        ComplexDescriber describer = ComplexDescriber.from(studentDO);
        describer.leftJoin(personDO).on(JoinCondition.equalTo(studentDO::getPersonId, personDO::getId));
        return describer;
    }

    public PersonDO getPersonDO() {
        return personDO;
    }

    public void setPersonDO(PersonDO personDO) {
        this.personDO = personDO;
    }

    public StudentDO getStudentDO() {
        return studentDO;
    }

    public void setStudentDO(StudentDO studentDO) {
        this.studentDO = studentDO;
    }
}
