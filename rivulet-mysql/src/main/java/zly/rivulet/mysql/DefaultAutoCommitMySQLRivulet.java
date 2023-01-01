package zly.rivulet.mysql;

import zly.rivulet.sql.SQLRivuletManager;

import java.sql.Connection;

public class DefaultAutoCommitMySQLRivulet extends MySQLRivulet {

    protected DefaultAutoCommitMySQLRivulet(SQLRivuletManager rivuletManager, Connection connection) {
        super(rivuletManager, connection);
    }
}
