package zly.rivulet.sql.pipeline;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.pipeline.ExecutePlan;
import zly.rivulet.base.pipeline.ResultInfo;
import zly.rivulet.base.utils.collector.FixedLengthStatementCollector;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.generator.SQLFish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUpdateOneExecutePlan extends ExecutePlan {
    @Override
    public Object plan(Blueprint blueprint, ParamManager paramManager, ResultInfo resultInfo, Connection connection) {
        Fish fish = super.getGenerator().generate(blueprint, paramManager);
        return super.beforeAndExecute(
            fish,
            () -> {
                SQLFish sqlFish = (SQLFish) fish;
                StatementCollector collector = new FixedLengthStatementCollector(sqlFish.getLength());
                sqlFish.getStatement().collectStatement(collector);
                try {
                    PreparedStatement statement = connection.prepareStatement(collector.toString());
                    return statement.executeUpdate(collector.toString());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }
}
