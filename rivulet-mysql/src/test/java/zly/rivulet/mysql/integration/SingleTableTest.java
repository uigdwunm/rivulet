package zly.rivulet.mysql.integration;

import org.junit.Test;
import zly.rivulet.base.Rivulet;
import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.warehouse.DefaultWarehouseManager;
import zly.rivulet.mysql.DefaultMySQLDataSourceRivuletManager;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.model.PersonDO;
import zly.rivulet.mysql.model.ProvinceDO;
import zly.rivulet.mysql.util.MySQLPrintUtils;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.desc.SortItem;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;

public class SingleTableTest extends BaseTest {

    @RivuletDesc("queryPersonTestDesc1")
    public WholeDesc queryPerson() {
        LocalDate start = LocalDate.of(1994, Month.JANUARY, 1);
        LocalDate end = LocalDate.of(1995, Month.JANUARY, 1);

        return QueryBuilder.query(PersonDO.class, PersonDO.class)
            .where(
                Condition.equalTo(PersonDO::getName, Param.staticOf("小明")),
                Condition.between(PersonDO::getBirthday, Param.staticOf(start), Param.staticOf(end))
            )
            .orderBy(SortItem.asc(PersonDO::getGender))
            .limit(Param.staticOf(999))
            .build();
    }

    @RivuletDesc("queryProvince")
    public WholeDesc queryProvince() {
        // 从参数中动态取值
        Param<Integer> provinceCodeParam = Param.of(Integer.class, "provinceCode", ParamCheckType.NATURE);

        return QueryBuilder.query(ProvinceDO.class, ProvinceDO.class)
            .where(
                Condition.equalTo(CheckCondition.notNull(provinceCodeParam), ProvinceDO::getCode, provinceCodeParam)
            )
            .build();
    }


    @Test
    public void query() {
        Rivulet rivulet = createDefaultRivuletManager().getRivulet();
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("provinceCode", 123);
        ProvinceDO provinceDO = rivulet.queryOneByDescKey("queryProvince", paramMap);


        Fish fish = rivuletManager.testParse("queryProvince");
        String statement = MySQLPrintUtils.commonPrint(fish);
        System.out.println(statement);
    }

    public RivuletManager createDefaultRivuletManager() {
        DefaultWarehouseManager defaultWarehouseManager = new DefaultWarehouseManager("zly.rivulet.mysql");
        return new DefaultMySQLDataSourceRivuletManager(
            new MySQLRivuletProperties(),
            new ConvertorManager(),
            defaultWarehouseManager,
            createDataSource()
        );
    }
}
