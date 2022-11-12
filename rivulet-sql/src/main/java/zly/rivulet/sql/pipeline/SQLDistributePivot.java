package zly.rivulet.sql.pipeline;

import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.pipeline.DistributePivot;
import zly.rivulet.base.pipeline.ResultInfo;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.CollectionInstanceCreator;

import java.sql.Connection;
import java.util.Collection;

public class SQLDistributePivot extends DistributePivot {

    private final SQLQueryOneExecutePlan queryOneExecutePlan = new SQLQueryOneExecutePlan();
    private final SQLQueryManyExecutePlan queryManyExecutePlan = new SQLQueryManyExecutePlan();

    private final SQLUpdateOneExecutePlan updateOneExecutePlan = new SQLUpdateOneExecutePlan();

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
        } else {
            // 增删改操作
            return updateOneExecutePlan.plan(blueprint, paramManager, resultInfo, connection);
        }
    }
}
