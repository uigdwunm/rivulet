package zly.rivulet.mysql.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.parser.Parser;
import zly.rivulet.base.pipeline.BeforeExecuteNode;
import zly.rivulet.base.pipeline.RunningPipeline;
import zly.rivulet.base.utils.collector.CommonStatementCollector;
import zly.rivulet.mysql.DefaultMySQLDataSourceRivuletManager;
import zly.rivulet.mysql.MySQLRivulet;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.model.PersonDO;
import zly.rivulet.sql.describer.query.SQLQueryBuilder;
import zly.rivulet.sql.parser.analyzer.DefaultSQLAnalyzer;

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
        DefaultMySQLDataSourceRivuletManager rivuletManager = new DefaultMySQLDataSourceRivuletManager(
            new MySQLRivuletProperties(),
            createDataSource()
        );
        rivuletManager.putInStorageByBasePackage("zly.rivulet.mysql");
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
        parser.addAnalyzer(new DefaultSQLAnalyzer());
        PersonDO p1 = new PersonDO();
        p1.setName("李小兰");
        p1.setBirthday(LocalDate.of(2022, Month.AUGUST, 12));
        p1.setGender(false);

//        int i = rivulet.insertOne(p1);
//        System.out.println(i);
        System.out.println("*********************");

        Blueprint blueprint = parser.parse(
            SQLQueryBuilder.query(PersonDO.class, PersonDO.class).build()
        );

        List<PersonDO> personDOList = rivulet.queryManyByBlueprint(blueprint, Collections.emptyMap());
        for (PersonDO personDO : personDOList) {
            System.out.println(personDO);
        }

        PersonDO personDO = personDOList.get(0);
        System.out.println(personDO);
        personDO.setBirthday(LocalDate.of(1998, Month.AUGUST, 8));
        rivulet.updateOneById(personDO);

        personDOList = rivulet.queryManyByBlueprint(blueprint, Collections.emptyMap());
        for (PersonDO p : personDOList) {
            System.out.println(p);
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
