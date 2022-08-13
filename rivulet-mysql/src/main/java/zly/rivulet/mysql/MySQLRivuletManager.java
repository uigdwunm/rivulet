package zly.rivulet.mysql;

import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.mysql.generator.MysqlGenerator;
import zly.rivulet.mysql.definer.MySQLDefiner;
import zly.rivulet.mysql.executor.MySQLExecutor;
import zly.rivulet.sql.parser.SqlParser;

import javax.sql.DataSource;

public class MySQLRivuletManager extends RivuletManager {

    public MySQLRivuletManager(
        MySQLRivuletProperties configProperties,
        ConvertorManager convertorManager,
        WarehouseManager warehouseManager,
        DataSource dataSource
    ) {
        super(
            new SqlParser(
                warehouseManager,
                new MySQLDefiner(convertorManager),
                configProperties,
                convertorManager
            ),
            new MysqlGenerator(
                configProperties,
                convertorManager
            ),
            new MySQLExecutor(dataSource),
            configProperties,
            convertorManager,
            warehouseManager
        );
    }

}
