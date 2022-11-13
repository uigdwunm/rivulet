package zly.rivulet.sql.pipeline;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.pipeline.BeforeExecuteNode;
import zly.rivulet.base.pipeline.ExecutePlan;
import zly.rivulet.base.utils.collector.FixedLengthStatementCollector;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.assigner.SQLQueryResultAssigner;
import zly.rivulet.sql.generator.SQLFish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLQueryOneExecutePlan extends ExecutePlan {

    private final Connection connection;

    public SQLQueryOneExecutePlan(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object plan(Blueprint blueprint, ParamManager paramManager, Generator generator, BeforeExecuteNode beforeExecuteNode) {
        Fish fish = generator.generate(blueprint, paramManager);
        return beforeExecuteNode.handle(
            fish,
            () -> {
                SQLFish sqlFish = (SQLFish) fish;
                StatementCollector collector = new FixedLengthStatementCollector(sqlFish.getLength());
                sqlFish.getStatement().collectStatement(collector);
                try {
                    PreparedStatement statement = connection.prepareStatement(collector.toString());
                    ResultSet resultSet = statement.executeQuery(collector.toString());
                    SQLQueryResultAssigner assigner = (SQLQueryResultAssigner) sqlFish.getBlueprint().getAssigner();
                    return assigner.getValue(resultSet, 0);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }
}
