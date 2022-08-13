package zly.rivulet.mysql.executor;

import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.executor.Executor;
import zly.rivulet.base.utils.collector.FixedLengthStatementCollector;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.MySQLFish;
import zly.rivulet.sql.assigner.SQLAssigner;

import javax.sql.DataSource;
import java.sql.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MySQLExecutor implements Executor {

    private final DataSource dataSource;

    public MySQLExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object queryOne(Fish fish, Assigner<?> assigner) {
        MySQLFish mySQLFish = (MySQLFish) fish;
        SQLAssigner sqlAssigner = (SQLAssigner) assigner;
        StatementCollector collector = new FixedLengthStatementCollector(mySQLFish.getLength());
        mySQLFish.getStatement().collectStatement(collector);
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(collector.toString());
            resultSet.next();
            return sqlAssigner.assign(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<Object> queryList(Fish fish, Assigner<?> assigner) {
        // TODO
        MySQLFish mySQLFish = (MySQLFish) fish;
        SQLAssigner sqlAssigner = (SQLAssigner) assigner;
        StatementCollector collector = new FixedLengthStatementCollector(mySQLFish.getLength());
        mySQLFish.getStatement().collectStatement(collector);
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(collector.toString());
            ResultSet resultSet = preparedStatement.executeQuery(collector.toString());
            ResultSetIterable<Object> iterable = new ResultSetIterable<>(
                resultSet,
                sqlAssigner,
                () -> {
                    try {
                        // 结束回调时回收资源
                        resultSet.close();
                        connection.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            return StreamSupport.stream(iterable.spliterator(), false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object executeUpdate(Fish fish) {
        MySQLFish mySQLFish = (MySQLFish) fish;
        StatementCollector collector = new FixedLengthStatementCollector(mySQLFish.getLength());
        mySQLFish.getStatement().collectStatement(collector);
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(collector.toString());
            return statement.executeUpdate(collector.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
