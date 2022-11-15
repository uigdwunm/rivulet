package zly.rivulet.mysql.pipeline.plan;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_model_meta.ModelBatchParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.pipeline.BeforeExecuteNode;
import zly.rivulet.base.pipeline.ExecutePlan;
import zly.rivulet.base.utils.collector.FixedLengthStatementCollector;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.sql.generator.SQLFish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MySQLBatchUpdateExecutePlan extends ExecutePlan {

    private final Connection connection;

    public MySQLBatchUpdateExecutePlan(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object plan(Blueprint blueprint, ParamManager paramManager, Generator generator, BeforeExecuteNode beforeExecuteNode) {
        ModelBatchParamManager batchParamManager = (ModelBatchParamManager) paramManager;
        Collection<Object> modelParamList = batchParamManager.getModelParamList();

        List<Integer> result = new ArrayList<>(modelParamList.size());
        Object last = null;
        for (Object model : modelParamList) {
            CommonParamManager subParamManager = batchParamManager.createCommonParamManager(last);
            Fish fish = generator.generate(blueprint, subParamManager);
            int[] ints = this.executeBatchUpdate(beforeExecuteNode, fish, connection, false);
            result.addAll(Arrays.stream(ints).boxed().collect(Collectors.toList()));

            last = model;
        }
        CommonParamManager subParamManager = batchParamManager.createCommonParamManager(last);
        Fish fish = generator.generate(blueprint, subParamManager);
        int[] ints = this.executeBatchUpdate(beforeExecuteNode, fish, connection, true);
        result.addAll(Arrays.stream(ints).boxed().collect(Collectors.toList()));

        return result;
    }

    protected int[] executeBatchUpdate(BeforeExecuteNode beforeExecuteNode, Fish fish, Connection connection, boolean isLast) {
        return (int[]) beforeExecuteNode.handle(
            fish,
            () -> {
                SQLFish sqlFish = (SQLFish) fish;
                StatementCollector collector = new FixedLengthStatementCollector(sqlFish.getLength());
                sqlFish.getStatement().collectStatement(collector);
                try {
                    PreparedStatement statement = connection.prepareStatement(collector.toString());
                    statement.addBatch();
                    if (isLast) {
                        return statement.executeBatch();
                    } else {
                        return null;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }
}
