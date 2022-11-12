package zly.rivulet.mysql.pipeline;

import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_model_meta.ModelBatchParamManager;
import zly.rivulet.base.pipeline.ResultInfo;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.CollectionInstanceCreator;
import zly.rivulet.sql.pipeline.SQLDistributePivot;
import zly.rivulet.sql.pipeline.SQLQueryManyExecutePlan;
import zly.rivulet.sql.pipeline.SQLQueryOneExecutePlan;
import zly.rivulet.sql.pipeline.SQLUpdateOneExecutePlan;

import java.sql.Connection;
import java.util.Collection;

public class MySQLDistributePivot extends SQLDistributePivot {

    private final SQLQueryOneExecutePlan queryOneExecutePlan = new SQLQueryOneExecutePlan();
    private final SQLQueryManyExecutePlan queryManyExecutePlan = new SQLQueryManyExecutePlan();

    private final SQLUpdateOneExecutePlan updateOneExecutePlan = new SQLUpdateOneExecutePlan();

    private final MySQLBatchInsertExecutePlan batchInsertExecutePlan = new MySQLBatchInsertExecutePlan();

    // 集合类型容器创建器
    protected final CollectionInstanceCreator collectionInstanceCreator = new CollectionInstanceCreator();

    @Override
    public Object distribute(Blueprint blueprint, ParamManager paramManager, ResultInfo resultInfo, Connection connection) {
        if (RivuletFlag.QUERY.equals(blueprint.getFlag())) {
            if (ClassUtils.isExtend(Collection.class, resultInfo.getResultType())) {
                // 结果是集合类型
                // 推断并创建集合容器
                Collection<Object> collection = collectionInstanceCreator.create(resultInfo.getResultType());
                resultInfo.setResultContainer(collection);

                // 执行
                return queryManyExecutePlan.plan(blueprint, paramManager, resultInfo, connection);
            } else {
                // 单个结果
                return queryOneExecutePlan.plan(blueprint, paramManager, resultInfo, connection);
            }
        } else if (paramManager instanceof ModelBatchParamManager) {
            // 批量插入操作
            batchInsertExecutePlan.plan(blueprint, paramManager, resultInfo, connection);
        } else {
            // 增删改操作
            return updateOneExecutePlan.plan(blueprint, paramManager, resultInfo, connection);
        }
    }
}
