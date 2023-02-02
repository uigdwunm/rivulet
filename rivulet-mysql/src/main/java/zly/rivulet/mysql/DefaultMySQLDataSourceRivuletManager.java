package zly.rivulet.mysql;

import zly.rivulet.base.warehouse.WarehouseManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultMySQLDataSourceRivuletManager extends MySQLRivuletManager {
    private final DataSource dataSource;

    public DefaultMySQLDataSourceRivuletManager(
        MySQLRivuletProperties configProperties,
        WarehouseManager warehouseManager,
        DataSource dataSource
    ) {
        super(configProperties, warehouseManager);
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
