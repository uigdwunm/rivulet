package zly.rivulet.mysql.integration;

import com.alibaba.fastjson2.JSON;
import org.junit.Test;
import zly.rivulet.base.Rivulet;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.convertor.StatementConvertor;
import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.mysql.discriber.function.MySQLFunction;
import zly.rivulet.mysql.model.PersonDO;
import zly.rivulet.mysql.model.ProvinceDO;
import zly.rivulet.mysql.util.MySQLPrintUtils;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.describer.query.SQLQueryBuilder;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.describer.query.desc.SortItem;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

public class SingleTableTest extends BaseTest {

    @RivuletDesc("queryPersonTestDesc1")
    public WholeDesc queryPerson() {
        LocalDate start = LocalDate.of(1994, Month.JANUARY, 1);
        LocalDate end = LocalDate.of(1995, Month.JANUARY, 1);
        return SQLQueryBuilder.query(PersonDO.class, PersonDO.class)
                .select(
                        Mapping.of(PersonDO::setName, MySQLFunction.toNchar(10).of(PersonDO::getId))
                )
                .where(
                    Condition.Equal.of(MySQLFunction.ADD.of(PersonDO::getId, PersonDO::getId), Param.staticOf(666)),
                    Condition.BETWEEN.of(PersonDO::getBirthday, Param.staticOf(start), Param.staticOf(end))
                )
                .orderBy(SortItem.asc(PersonDO::getGender))
                .limit(Param.staticOf(999))
                .build();
    }


    @Test
    public void query11() {
        Rivulet rivulet = rivuletManager.getRivulet();
        Fish fish = rivuletManager.testParse("queryPersonTestDesc1");
        String statement = MySQLPrintUtils.commonPrint(fish);
        System.out.println(statement);
    }

    @RivuletDesc("queryByCode")
    public WholeDesc queryByCode() {
        // select * from t_province where name = '小明' and (code > 2 or code <= 8)
        return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
            .where(
                Condition.IN.of(
                    ProvinceDO::getCode,
                    SQLQueryBuilder.query(ProvinceDO.class, Integer.class).selectOne(ProvinceDO::getCode).build()
                )
            ).build();
//        return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
//            .where(
//                Condition.Equal.of(MySQLFunction.Cast.toNchar(10).of(ProvinceDO::getCode), Param.staticOf("2"))
//            ).build();
    }

    @Test
    public void test() {
        Fish fish = rivuletManager.testParse("queryByCode");
        String statement = MySQLPrintUtils.commonPrint(fish);
        System.out.println(statement);
    }

    @RivuletDesc("queryProvince")
    public WholeDesc queryProvince() {
        return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
            .select(
                Mapping.of(ProvinceDO::setCode, Param.staticOf(112)),
                Mapping.of(ProvinceDO::setName, ProvinceDO::getName)
            ).where(
                Condition.Equal.of(ProvinceDO::getCode, Param.of(Integer.class, "province.code"))
            ).skit(Param.staticOf(0)).limit(Param.staticOf(1)).build();
    }


    @Test
    public void query() {
//        Fish fish = rivuletManager.testParse(queryProvince());
//        String statement = MySQLPrintUtils.commonPrint(fish);
//        System.out.println(statement);
        Rivulet rivulet = rivuletManager.getRivulet();
        List<ProvinceDO> queryProvince = rivulet.queryManyByDescKey("queryProvince", Collections.emptyMap());
        for (ProvinceDO provinceDO : queryProvince) {
            System.out.println(provinceDO);
        }
    }

    @RivuletDesc("queryById")
    public WholeDesc queryById() {
        // select * from t_person where id = 2
        return SQLQueryBuilder.query(PersonDO.class, PersonDO.class)
            .where(Condition.Equal.of(PersonDO::getId, Param.staticOf(2)))
            .build();
    }

    @Test
    public void testQueryById() {
        ConvertorManager convertorManager = rivuletManager.getConvertorManager();
        convertorManager.registerSuperClassConvertor(
            new StatementConvertor<ProvinceDO>() {
                @Override
                public String convert(ProvinceDO originData) {
                    return "'" + JSON.toJSONString(originData) + "'";
                }
            },
            new ResultConvertor<String, ProvinceDO>() {
                @Override
                public ProvinceDO convert(String originData) {
                    return JSON.parseObject(originData, ProvinceDO.class);
                }
            }
        );
        Rivulet rivulet = rivuletManager.getRivulet();
        PersonDO o = rivulet.queryOneByDescKey("queryById", Collections.singletonMap("id", 18L));
        System.out.println(o);
    }

    @RivuletDesc("count")
    public WholeDesc count() {
        SQLFunction<Object, String> of = MySQLFunction.COUNT.of(Param.staticOf("1"));
        return SQLQueryBuilder.query(PersonDO.class, Integer.class)
            .selectOne(
                MySQLFunction.COUNT.of(Param.staticOf(1))
            ).build();
    }

    @Test
    public void testCount() {
        Rivulet rivulet = rivuletManager.getRivulet();
        Object count = rivulet.queryOneByDescKey("count", Collections.emptyMap());
        System.out.println(count);
    }

    @Test
    public void testInsert() {
        ConvertorManager convertorManager = rivuletManager.getConvertorManager();
        convertorManager.registerSuperClassConvertor(
            new StatementConvertor<ProvinceDO>() {
                @Override
                public String convert(ProvinceDO originData) {
                    return "'" + JSON.toJSONString(originData) + "'";
                }
            },
            new ResultConvertor<String, ProvinceDO>() {
                @Override
                public ProvinceDO convert(String originData) {
                    return JSON.parseObject(originData, ProvinceDO.class);
                }
            }
        );
        PersonDO personDO = new PersonDO();
        personDO.setBirthday(LocalDate.now());
        personDO.setName("张小圆");
        personDO.setGender(false);
        ProvinceDO provinceDO = new ProvinceDO();
        provinceDO.setCode(123);
        provinceDO.setName("西红市");
        personDO.setExtra(provinceDO);
        Rivulet rivulet = rivuletManager.getRivulet();
        rivulet.insertOne(personDO);

        PersonDO o = rivulet.queryOneByDescKey("queryById", Collections.singletonMap("id", 18L));
        System.out.println(o);
    }

}
