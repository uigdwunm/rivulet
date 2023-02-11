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

public class SingleTableTest extends BaseTest {

    @RivuletDesc("queryPersonTestDesc1")
    public WholeDesc queryPerson() {
        LocalDate start = LocalDate.of(1994, Month.JANUARY, 1);
        LocalDate end = LocalDate.of(1995, Month.JANUARY, 1);
        return SQLQueryBuilder.query(PersonDO.class, PersonDO.class)
                .select(
                        Mapping.of(PersonDO::setName, MySQLFunction.Cast.toNchar(10).of(PersonDO::getId))
                )
                .where(
                    Condition.Equal.of(MySQLFunction.Arithmetical.ADD.of(PersonDO::getId, PersonDO::getId), Param.staticOf(666)),
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

    @RivuletDesc("queryProvince")
    public WholeDesc queryProvince() {
        // 从参数中动态取值
        Param<Integer> provinceCodeParam = Param.of(Integer.class, "provinceCode", ParamCheckType.NATURE);

        return SQLQueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
            .where(
                Condition.Equal.of(CheckCondition.notNull(provinceCodeParam), ProvinceDO::getCode, provinceCodeParam)
            ).build();
    }


    @Test
    public void query() {
//        Rivulet rivulet = createDefaultRivuletManager().getRivulet();
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("provinceCode", 123);
//        ProvinceDO provinceDO = rivulet.queryOneByDescKey("queryProvince", paramMap);
//
//
//        Fish fish = rivuletManager.testParse("queryProvince");
//        String statement = MySQLPrintUtils.commonPrint(fish);
//        System.out.println(statement);
    }

    @RivuletDesc("queryById")
    public WholeDesc queryById() {
        return SQLQueryBuilder.query(PersonDO.class, PersonDO.class)
            .where(Condition.Equal.of(PersonDO::getId, Param.of(Long.class, "id")))
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
        SQLFunction<Object, String> of = MySQLFunction.Aggregate.COUNT.of(Param.staticOf("1"));
        return SQLQueryBuilder.query(PersonDO.class, Integer.class)
            .selectOne(
                MySQLFunction.Aggregate.COUNT.of(Param.staticOf(1))
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
