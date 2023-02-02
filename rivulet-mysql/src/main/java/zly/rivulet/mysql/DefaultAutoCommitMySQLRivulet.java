package zly.rivulet.mysql;

import zly.rivulet.base.exception.ExecuteException;
import zly.rivulet.sql.SQLRivuletManager;

import java.sql.Connection;
import java.sql.SQLException;

public class DefaultAutoCommitMySQLRivulet extends MySQLRivulet {
    private final Connection connection;

    private volatile boolean isClosed = false;

    protected DefaultAutoCommitMySQLRivulet(SQLRivuletManager rivuletManager, Connection connection) {
        super(rivuletManager);
        this.connection = connection;
    }

    @Override
    protected Connection useConnection() {
        if (isClosed) {
            throw ExecuteException.rivuletIsClosed();
        }
        return this.connection;
    }

    @Override
    public void close() {
        try {
            this.isClosed = true;
            this.useConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
