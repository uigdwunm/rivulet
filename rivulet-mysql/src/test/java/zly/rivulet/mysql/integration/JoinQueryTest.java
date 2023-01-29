package zly.rivulet.mysql.integration;

import org.junit.Test;
import zly.rivulet.base.Rivulet;
import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.mysql.model.join.CityProvinceJoin;
import zly.rivulet.mysql.model.join.StudentJoinPerson;
import zly.rivulet.mysql.model.vo.CityInfo;
import zly.rivulet.mysql.model.vo.StudentVO;
import zly.rivulet.mysql.util.MySQLPrintUtils;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.desc.Mapping;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class JoinQueryTest extends BaseTest {

    @RivuletDesc("queryCityProvinceJoin")
    public WholeDesc queryCityProvinceJoin() {
        // 从参数中动态取值
        Param<Integer> cityCodeParam = Param.of(Integer.class, "cityCode", ParamCheckType.NATURE);

        return QueryBuilder.query(CityProvinceJoin.class, CityProvinceJoin.class)
            .where(
                Condition.Equal.of(x -> x.getCityDO().getCode(), cityCodeParam)
            ).build();
    }

    @RivuletDesc("queryCityProvinceJoin2")
    public WholeDesc queryCityProvinceJoin2() {
        // 从参数中动态取值
        Param<Integer> cityCodeParam = Param.of(Integer.class, "cityCode", ParamCheckType.NATURE);

        return QueryBuilder.query(CityProvinceJoin.class, CityInfo.class)
            .select(
                Mapping.of(CityInfo::setCityCode, x -> x.getCityDO().getCode()),
                Mapping.of(CityInfo::setCityName, x -> x.getCityDO().getName()),
                Mapping.of(CityInfo::setProvinceCode, x -> x.getProvinceDO().getCode()),
                Mapping.of(CityInfo::setProvinceName, x -> x.getProvinceDO().getName())
            )
            .where(Condition.Equal.of(x -> x.getCityDO().getCode(), cityCodeParam))
            .build();
    }

    @Test
    public void parseJoinQuery() {
        Fish fish = rivuletManager.testParse(queryCityProvinceJoin());

        String statement = MySQLPrintUtils.formatPrint(fish);
        System.out.println(statement);
    }

    @Test
    public void joinQuery() {
//        Rivulet rivulet = rivuletManager.getRivulet();
//        Blueprint blueprint = rivulet.parse(
//            QueryBuilder.query(StudentJoinPerson.class, StudentVO.class)
//                .select(
//                    Mapping.of(StudentVO::setName, x -> x.getPersonDO().getName()),
//                    Mapping.of(StudentVO::setBirthday, x -> x.getPersonDO().getBirthday()),
//                    Mapping.of(StudentVO::setGender, x -> x.getPersonDO().getGender()),
//                    Mapping.of(StudentVO::setStudyTarget, x -> x.getStudentDO().getTarget()),
//                    Mapping.of(StudentVO::setStudyTime, x -> x.getStudentDO().getCreateTime())
//                )
//                .where(
//                    Condition.BETWEEN.of(x -> x.getStudentDO().getCreateTime(), Param.staticOf(LocalDateTime.now().minusDays(100)), Param.staticOf(LocalDateTime.now()))
//                )
//                .build()
//        );

//        List<StudentVO> vos = rivulet.queryManyByBlueprint(blueprint, Collections.emptyMap());
//        for (StudentVO vo : vos) {
//            System.out.println(vo);
//        }
    }
}
