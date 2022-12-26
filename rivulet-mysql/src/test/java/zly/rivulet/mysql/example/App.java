package zly.rivulet.mysql.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.parser.Parser;
import zly.rivulet.base.pipeline.BeforeExecuteNode;
import zly.rivulet.base.pipeline.RunningPipeline;
import zly.rivulet.base.utils.collector.CommonStatementCollector;
import zly.rivulet.base.warehouse.DefaultWarehouseManager;
import zly.rivulet.mysql.MySQLDataSourceRivuletManager;
import zly.rivulet.mysql.MySQLRivulet;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.example.model.PersonDO;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.parser.analyzer.DefaultAnalyzer;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class App {

    public static DataSource createDataSource() {
//        Class.forName("com.mysql.jdbc.Driver");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://rm-bp1kms1f90gguo416po.rwlb.rds.aliyuncs.com:3306/tt_database?autoReconnect=true&useSSL=false");
        config.setUsername("v587");
        config.setPassword("v587V%*&");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);

    }

    public static void main(String[] args) {
        DefaultWarehouseManager defaultWarehouseManager = new DefaultWarehouseManager("zly.rivulet.mysql");
        // todo beanManager配置扫包
        MySQLDataSourceRivuletManager rivuletManager = new MySQLDataSourceRivuletManager(
            new MySQLRivuletProperties(),
            new ConvertorManager(),
            defaultWarehouseManager,
            createDataSource()
        );
        RunningPipeline runningPipeline = rivuletManager.getRunningPipeline();
        runningPipeline.addBeforeExecuteNode(new BeforeExecuteNode() {
            @Override
            public Object handle(Fish fish, Supplier<Object> executor) {
                Statement statement = fish.getStatement();
                CommonStatementCollector commonStatementCollector = new CommonStatementCollector();
                statement.collectStatementOrCache(commonStatementCollector);
                System.out.println(commonStatementCollector);
                return nextHandle(fish, executor);
            }
        });
        Parser parser = rivuletManager.getParser();
        MySQLRivulet rivulet = rivuletManager.getRivulet();
        parser.addAnalyzer(new DefaultAnalyzer());
        PersonDO p1 = new PersonDO();
        p1.setName("李小兰");
        p1.setBirthday(LocalDate.of(2022, Month.AUGUST, 12));
        p1.setGender(false);

//        int i = rivuletManager.insertOne(p1);
//        System.out.println(i);
        System.out.println("*********************");
//        PersonDO personDO = rivuletManager.queryById(1L, PersonDO.class);
//        System.out.println(personDO);

        Blueprint blueprint = parser.parse(
            QueryBuilder.query(PersonDO.class, PersonDO.class).build()
        );
        List<PersonDO> personDOList = rivulet.queryManyByBlueprint(blueprint, Collections.emptyMap());
        for (PersonDO personDO : personDOList) {
            System.out.println(personDO);
        }


//        Fish test = rivuletManager.testParse(PersonDescConfig.queryPerson());
//
//        Statement statement = test.getStatement();
//        MySQLFormatStatementCollector formatStatementCollector = new MySQLFormatStatementCollector();
//        statement.collectStatement(formatStatementCollector);
//        System.out.println(formatStatementCollector);
//
//
//        SQLCommonStatementCollector commonStatementCollector = new SQLCommonStatementCollector();
//        statement.collectStatementOrCache(commonStatementCollector);
//        System.out.println(commonStatementCollector);
    }

}
