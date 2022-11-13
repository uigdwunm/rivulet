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
import java.util.Collection;

public class SQLQueryManyExecutePlan extends ExecutePlan {

    private final Connection connection;

    private final Collection<Object> resultCollection;

    public SQLQueryManyExecutePlan(Connection connection, Collection<Object> resultCollection) {
        this.connection = connection;
        this.resultCollection = resultCollection;
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
                    SQLQueryResultAssigner assigner = (SQLQueryResultAssigner) blueprint.getAssigner();

                    // 执行查询
                    ResultSet resultSet = statement.executeQuery(collector.toString());

                    // 拼装结果
                    while (resultSet.next()) {
                        resultCollection.add(assigner.getValue(resultSet, 0));
                    }
                    // 回收资源
                    resultSet.close();

                    return resultCollection;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }
}
