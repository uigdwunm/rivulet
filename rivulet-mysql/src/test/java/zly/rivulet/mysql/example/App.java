package zly.rivulet.mysql.example;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.warehouse.DefaultWarehouseManager;
import zly.rivulet.mysql.MySQLRivuletManager;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.example.config.PersonDescConfig;
import zly.rivulet.mysql.util.MySQLFormatStatementCollector;
import zly.rivulet.sql.utils.collector.SQLCommonStatementCollector;

public class App {

    public static void main(String[] args) {

        DefaultWarehouseManager defaultWarehouseManager = new DefaultWarehouseManager("zly.rivulet.mysql");
        // todo beanManager配置扫包
        MySQLRivuletManager rivuletManager = new MySQLRivuletManager(
            new MySQLRivuletProperties(),
            new ConvertorManager(),
            defaultWarehouseManager,
            null
        );

        Fish test = rivuletManager.testParse(PersonDescConfig.queryPerson());

        Statement statement = test.getStatement();
        MySQLFormatStatementCollector formatStatementCollector = new MySQLFormatStatementCollector();
        statement.collectStatement(formatStatementCollector);
        System.out.println(formatStatementCollector);


        SQLCommonStatementCollector commonStatementCollector = new SQLCommonStatementCollector();
        statement.collectStatementOrCache(commonStatementCollector);
        System.out.println(commonStatementCollector);
    }

}
