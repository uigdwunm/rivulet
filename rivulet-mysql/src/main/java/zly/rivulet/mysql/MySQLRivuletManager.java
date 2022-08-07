package zly.rivulet.mysql;

import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.mysql.definer.MySQLDefiner;
import zly.rivulet.mysql.runparser.MysqlAssemblyLine;
import zly.rivulet.sql.preparser.SqlPreParser;

public class MySQLRivuletManager extends RivuletManager {

    public MySQLRivuletManager(MySQLRivuletProperties configProperties, ConvertorManager convertorManager, WarehouseManager warehouseManager) {
        super(
            new MysqlAssemblyLine(configProperties, convertorManager),
            null,
            null,
            configProperties,
            convertorManager,
            warehouseManager,
            new SqlPreParser(
                warehouseManager,
                new MySQLDefiner(convertorManager),
                configProperties,
                convertorManager
            )
        );
    }

}
