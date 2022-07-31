package zly.rivulet.mysql.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HikariConnectionPool {

    public static Connection getConnection() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://106.12.132.148:33306/train_camp");
        config.setUsername("train");
        config.setPassword("train@123");
//        config.setJdbcUrl("jdbc:mysql://localhost:3306/simpsons");
//        config.setUsername("bart");
//        config.setPassword("51mp50n");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        try {
            HikariDataSource ds = new HikariDataSource(config);
            return ds.getConnection();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from czy_user");
        while (resultSet.next()) {
//            resultSet.getString()
        }

    }
}
