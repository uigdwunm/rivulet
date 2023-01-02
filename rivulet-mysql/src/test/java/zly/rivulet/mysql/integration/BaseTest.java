package zly.rivulet.mysql.integration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.BeforeClass;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.warehouse.DefaultWarehouseManager;
import zly.rivulet.mysql.DefaultMySQLDataSourceRivuletManager;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.sql.SQLRivuletManager;

import javax.sql.DataSource;

public abstract class BaseTest {

    protected static SQLRivuletManager rivuletManager;

    public static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://rm-bp1kms1f90gguo416po.rwlb.rds.aliyuncs.com:3306/tt_database?autoReconnect=true&useSSL=false");
        config.setUsername("v587");
        config.setPassword("v587V%*&");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }

    @BeforeClass
    public static void createRivuletManager() {
        DefaultWarehouseManager defaultWarehouseManager = new DefaultWarehouseManager("zly.rivulet.mysql");
        rivuletManager = new DefaultMySQLDataSourceRivuletManager(
            new MySQLRivuletProperties(),
            new ConvertorManager(),
            defaultWarehouseManager,
            createDataSource()
        );
    }
}
