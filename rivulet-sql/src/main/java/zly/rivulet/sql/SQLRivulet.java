package zly.rivulet.sql;

import zly.rivulet.base.Rivulet;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.exception.ExecuteException;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.SimpleParamManager;
import zly.rivulet.base.pipeline.ExecutePlan;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;
import zly.rivulet.sql.pipeline.SQLQueryManyExecutePlan;
import zly.rivulet.sql.pipeline.SQLQueryOneExecutePlan;
import zly.rivulet.sql.pipeline.SQLUpdateOneExecutePlan;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class SQLRivulet extends Rivulet {

    private final TwofoldConcurrentHashMap<RivuletFlag, Class<?>, ExecutePlan> rivuletFlagClassExecutePlanMap;

    private final Connection connection;

    private volatile boolean isClosed = false;

    protected SQLRivulet(SQLRivuletManager rivuletManager, Connection connection) {
        super(rivuletManager);
        this.rivuletFlagClassExecutePlanMap = rivuletManager.rivuletFlagClassExecutePlanMap;
        this.connection = connection;
    }


    protected Connection useConnection() {
        if (isClosed) {
            throw ExecuteException.rivuletIsClosed();
        }
        return this.connection;
    }

    public void close() {
        try {
            this.isClosed = true;
            this.useConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void commit() {
        try {
            this.useConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollback() {
        try {
            this.useConnection().rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected ExecutePlan createExecutePlan(RivuletFlag rivuletFlag, Class<?> returnType) {
        ExecutePlan executePlan = rivuletFlagClassExecutePlanMap.get(rivuletFlag, returnType);
        if (executePlan != null) {
            return executePlan;
        }
        if (RivuletFlag.QUERY.equals(rivuletFlag)) {
            if (ClassUtils.isExtend(Collection.class, returnType)) {
                Collection<Object> collection = super.collectionInstanceCreator.create(returnType);
                return new SQLQueryManyExecutePlan(this.useConnection(), collection);
            } else {
                return new SQLQueryOneExecutePlan(this.useConnection());
            }
        } else {
            return new SQLUpdateOneExecutePlan(this.useConnection());
        }
    }

    /*-------------------------------------------------- 查询单个 --------------------------------------------------*/

    @Override
    public <T> T queryOneByBlueprint(Blueprint blueprint, ParamManager paramManager) {
        // 单个查询
        SQLQueryOneExecutePlan sqlQueryOneExecutePlan = new SQLQueryOneExecutePlan(this.useConnection());
        return runningPipeline.go(blueprint, paramManager, sqlQueryOneExecutePlan);
    }

    /*-------------------------------------------------- 查询多个 --------------------------------------------------*/

    /**
     * 通过blueprint查询多个，自带结果容器
     **/
    @Override
    public <T> void queryManyByBlueprint(Blueprint blueprint, ParamManager paramManager, Collection<T> resultContainer) {
        // 批量查询
        SQLQueryManyExecutePlan executePlan = new SQLQueryManyExecutePlan(this.useConnection(), (Collection<Object>) resultContainer);
        this.runningPipeline.go(blueprint, paramManager, executePlan);
    }



    /*-------------------------------------------------- 新增 --------------------------------------------------*/

    @Override
    public <T> int insertOneByBlueprint(Blueprint blueprint, ParamManager paramManager) {
        SQLUpdateOneExecutePlan executePlan = new SQLUpdateOneExecutePlan(this.useConnection());
        return runningPipeline.go(blueprint, paramManager, executePlan);
    }

    @Override
    public abstract <T> List<Integer> batchInsert(Collection<T> batchModel, Class<T> dOModelClass);

    /*-------------------------------------------------- 更新 --------------------------------------------------*/

    @Override
    public int updateByBlueprint(Blueprint blueprint, ParamManager paramManager) {
        SQLUpdateOneExecutePlan executePlan = new SQLUpdateOneExecutePlan(this.useConnection());
        return runningPipeline.go(blueprint, paramManager, executePlan);
    }

    /**
     * 批量通过主键id更新
     **/
    @Override
    public abstract <T> int batchUpdateById(Collection<T> obj, Class<T> dOModelClass);


    /*-------------------------------------------------- 删除 --------------------------------------------------*/

    @Override
    public int deleteByBlueprint(Blueprint blueprint, Map<String, Object> params) {
        SQLUpdateOneExecutePlan executePlan = new SQLUpdateOneExecutePlan(this.useConnection());
        return runningPipeline.go(blueprint, new SimpleParamManager(params), executePlan);
    }
}
