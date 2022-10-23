package zly.rivulet.mysql;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.mysql.definer.MySQLDefiner;
import zly.rivulet.mysql.generator.MysqlGenerator;
import zly.rivulet.sql.SQLRivuletManager;
import zly.rivulet.sql.parser.SqlParser;

import javax.sql.DataSource;

public class MySQLRivuletManager extends SQLRivuletManager {

    public MySQLRivuletManager(
        MySQLRivuletProperties configProperties,
        ConvertorManager convertorManager,
        WarehouseManager warehouseManager,
        DataSource dataSource
    ) {
        super(
            new MysqlGenerator(
                configProperties,
                new SqlParser(
                    warehouseManager,
                    new MySQLDefiner(convertorManager),
                    configProperties,
                    convertorManager
                )
            ),
            warehouseManager,
            dataSource
        );
    }


}
