package zly.rivulet.mysql.executor;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.executor.Executor;
import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.utils.StatementCollector;
import zly.rivulet.mysql.runparser.MySQLFish;
import zly.rivulet.sql.assigner.SQLAssigner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
        StatementCollector collector = new StatementCollector(1000);
        mySQLFish.getStatement().collectStatement(collector);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(collector.toString());
        // TODO 参数
        resultSet.next();
        return sqlAssigner.assign(resultSet);
    }

    @Override
    public Stream<Object> queryList(Fish fish, Assigner<?> assigner) {
        MySQLFish mySQLFish = (MySQLFish) fish;
        SQLAssigner sqlAssigner = (SQLAssigner) assigner;
        StatementCollector collector = new StatementCollector(1000);
        mySQLFish.getStatement().collectStatement(collector);
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement();
        ResultSet resultSet = preparedStatement.executeQuery(collector.toString());
        return StreamSupport.stream(new ResultSetIterable<>(resultSet, sqlAssigner).spliterator(), false);
    }

    @Override
    public int executeUpdate(Fish fish) {
        return 0;
    }
}
