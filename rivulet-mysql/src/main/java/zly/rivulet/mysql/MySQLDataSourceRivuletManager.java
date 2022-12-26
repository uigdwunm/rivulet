package zly.rivulet.mysql;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.mysql.definer.MySQLDefiner;
import zly.rivulet.mysql.generator.MysqlGenerator;
import zly.rivulet.sql.SQLRivuletManager;
import zly.rivulet.sql.parser.SqlParser;

import javax.sql.DataSource;
import java.sql.SQLException;

public class MySQLDataSourceRivuletManager extends SQLRivuletManager {
    private final DataSource dataSource;

    public MySQLDataSourceRivuletManager(
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
            warehouseManager
        );
        this.dataSource = dataSource;
    }

    @Override
    public MySQLRivulet getRivulet() {
        try {
            return new MySQLRivulet(this, dataSource.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
