package zly.rivulet.mysql;

import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.analyzer.Analyzer;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.mysql.definer.MySQLDefiner;
import zly.rivulet.mysql.assembly_line.MysqlAssemblyLine;
import zly.rivulet.sql.parser.SqlParser;

public class MySQLRivuletManager extends RivuletManager {

    public MySQLRivuletManager(
        MySQLRivuletProperties configProperties,
        ConvertorManager convertorManager,
        WarehouseManager warehouseManager,
        Analyzer analyzer
    ) {
        super(
            new MysqlAssemblyLine(
                configProperties,
                convertorManager
            ),
            analyzer,
            null,
            configProperties,
            convertorManager,
            warehouseManager,
            new SqlParser(
                warehouseManager,
                new MySQLDefiner(convertorManager),
                analyzer,
                configProperties,
                convertorManager
            )
        );
    }

}
