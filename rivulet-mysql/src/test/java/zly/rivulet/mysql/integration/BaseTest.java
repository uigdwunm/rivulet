package zly.rivulet.mysql.integration;

import org.junit.BeforeClass;
import zly.rivulet.base.parser.Parser;
import zly.rivulet.mysql.DefaultMySQLDataSourceRivuletManager;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.sql.SQLRivuletManager;
import zly.rivulet.sql.parser.analyzer.DefaultSQLAnalyzer;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public abstract class BaseTest {

    protected static SQLRivuletManager rivuletManager;


    @BeforeClass
    public static void createRivuletManager() {
        rivuletManager = new DefaultMySQLDataSourceRivuletManager(
            new MySQLRivuletProperties(),
            createDataSource()
        );
        rivuletManager.putInStorageByBasePackage("zly.rivulet.mysql");

        // 过滤掉最外层查询参数的别名
        Parser parser = rivuletManager.getParser();
        parser.addAnalyzer(new DefaultSQLAnalyzer());
    }

    /**
     * 弄一个空的DataSource，这部分测试不需要
     **/
    public static DataSource createDataSource() {

        return new DataSource() {
            @Override
            public PrintWriter getLogWriter() throws SQLException {
                return null;
            }

            @Override
            public void setLogWriter(PrintWriter out) throws SQLException {

            }

            @Override
            public void setLoginTimeout(int seconds) throws SQLException {

            }

            @Override
            public int getLoginTimeout() throws SQLException {
                return 0;
            }

            @Override
            public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> iface) throws SQLException {
                return null;
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                return false;
            }

            @Override
            public Connection getConnection() throws SQLException {
                return null;
            }

            @Override
            public Connection getConnection(String username, String password) throws SQLException {
                return null;
            }
        };
    }
}
