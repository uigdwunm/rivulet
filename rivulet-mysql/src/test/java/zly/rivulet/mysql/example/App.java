package zly.rivulet.mysql.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.warehouse.DefaultWarehouseManager;
import zly.rivulet.mysql.MySQLRivuletManager;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.example.model.PersonDO;

import javax.sql.DataSource;

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
        MySQLRivuletManager rivuletManager = new MySQLRivuletManager(
            new MySQLRivuletProperties(),
            new ConvertorManager(),
            defaultWarehouseManager,
            createDataSource()
        );
        rivuletManager.preParseAll();
        rivuletManager.warmUpAll();
        PersonDO personDO = rivuletManager.queryById(1L, PersonDO.class);
        System.out.println(personDO);


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
