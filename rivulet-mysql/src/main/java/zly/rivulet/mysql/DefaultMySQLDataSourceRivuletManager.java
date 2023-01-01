package zly.rivulet.mysql;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.mysql.definer.MySQLDefiner;
import zly.rivulet.mysql.generator.MysqlGenerator;
import zly.rivulet.sql.SQLRivuletManager;
import zly.rivulet.sql.parser.SqlParser;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultMySQLDataSourceRivuletManager extends SQLRivuletManager {
    private final DataSource dataSource;

    public DefaultMySQLDataSourceRivuletManager(
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
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(true);
            return new DefaultAutoCommitMySQLRivulet(this, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
