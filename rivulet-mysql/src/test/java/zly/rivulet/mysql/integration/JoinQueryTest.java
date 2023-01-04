package zly.rivulet.mysql.integration;

import org.junit.Test;
import zly.rivulet.base.Rivulet;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.utils.collector.CommonStatementCollector;
import zly.rivulet.mysql.example.model.join.StudentJoinPerson;
import zly.rivulet.mysql.example.model.vo.StudentVO;
import zly.rivulet.mysql.util.MySQLFormatStatementCollector;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.desc.Mapping;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class JoinQueryTest extends BaseTest {

    @Test
    public void parseJoinQuery() {
        Fish fish = rivuletManager.testParse(
            QueryBuilder.query(StudentJoinPerson.class, StudentVO.class)
                .select(
                    Mapping.of(StudentVO::setName, x -> x.getPersonDO().getName()),
                    Mapping.of(StudentVO::setBirthday, x -> x.getPersonDO().getBirthday()),
                    Mapping.of(StudentVO::setGender, x -> x.getPersonDO().getGender()),
                    Mapping.of(StudentVO::setStudyTarget, x -> x.getStudentDO().getTarget()),
                    Mapping.of(StudentVO::setStudyTime, x -> x.getStudentDO().getCreateTime())
                )
                .where(
                    Condition.between(x -> x.getStudentDO().getCreateTime(), Param.staticOf(LocalDateTime.now().minusDays(100)), Param.staticOf(LocalDateTime.now()))
                )
                .build()
        );

        Statement statement = fish.getStatement();
        MySQLFormatStatementCollector mySQLFormatStatementCollector = new MySQLFormatStatementCollector();
        statement.collectStatement(mySQLFormatStatementCollector);
        System.out.println(mySQLFormatStatementCollector);
    }

    @Test
    public void joinQuery() {
        Rivulet rivulet = rivuletManager.getRivulet();
        Blueprint blueprint = rivulet.parse(
            QueryBuilder.query(StudentJoinPerson.class, StudentVO.class)
                .select(
                    Mapping.of(StudentVO::setName, x -> x.getPersonDO().getName()),
                    Mapping.of(StudentVO::setBirthday, x -> x.getPersonDO().getBirthday()),
                    Mapping.of(StudentVO::setGender, x -> x.getPersonDO().getGender()),
                    Mapping.of(StudentVO::setStudyTarget, x -> x.getStudentDO().getTarget()),
                    Mapping.of(StudentVO::setStudyTime, x -> x.getStudentDO().getCreateTime())
                )
                .where(
                    Condition.between(x -> x.getStudentDO().getCreateTime(), Param.staticOf(LocalDateTime.now().minusDays(100)), Param.staticOf(LocalDateTime.now()))
                )
                .build()
        );

        List<StudentVO> vos = rivulet.queryManyByBlueprint(blueprint, Collections.emptyMap());
        for (StudentVO vo : vos) {
            System.out.println(vo);
        }
    }
}
