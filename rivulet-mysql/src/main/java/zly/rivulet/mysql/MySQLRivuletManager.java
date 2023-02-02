package zly.rivulet.mysql;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.mysql.definer.MySQLDefiner;
import zly.rivulet.mysql.generator.MysqlGenerator;
import zly.rivulet.sql.SQLRivuletManager;
import zly.rivulet.sql.parser.SqlParser;

public abstract class MySQLRivuletManager extends SQLRivuletManager {

    protected MySQLRivuletManager(MySQLRivuletProperties configProperties, WarehouseManager warehouseManager) {
        this(configProperties, cretateDefaultConvertorManager(), warehouseManager);
    }

    protected MySQLRivuletManager(MySQLRivuletProperties configProperties, ConvertorManager convertorManager, WarehouseManager warehouseManager) {
        this(configProperties, new MySQLDefiner(convertorManager), warehouseManager);
    }

    protected MySQLRivuletManager(MySQLRivuletProperties configProperties, MySQLDefiner mySQLDefiner, WarehouseManager warehouseManager) {
        this(configProperties, new SqlParser(warehouseManager, mySQLDefiner, configProperties), warehouseManager);
    }

    protected MySQLRivuletManager(MySQLRivuletProperties configProperties, SqlParser sqlParser, WarehouseManager warehouseManager) {
        super(new MysqlGenerator(configProperties, sqlParser), warehouseManager);
    }
}
