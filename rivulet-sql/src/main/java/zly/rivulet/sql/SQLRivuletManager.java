package zly.rivulet.sql;

import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.ExecuteException;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.pipeline.ExecutePlan;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.pipeline.SQLQueryManyExecutePlan;
import zly.rivulet.sql.pipeline.SQLQueryOneExecutePlan;
import zly.rivulet.sql.pipeline.SQLUpdateOneExecutePlan;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class SQLRivuletManager extends RivuletManager {

    private final DataSource dataSource;
    private final TwofoldConcurrentHashMap<RivuletFlag, Class<?>, ExecutePlan> rivuletFlagClassExecutePlanMap;

    protected SQLRivuletManager(
        Generator generator,
        WarehouseManager warehouseManager,
        DataSource dataSource
    ) {
        super(generator.getParser(), generator, generator.getProperties(), generator.getConvertorManager(), warehouseManager);
        rivuletFlagClassExecutePlanMap = new TwofoldConcurrentHashMap<>();
        this.dataSource = dataSource;
    }

    @Override
    public Blueprint parse(WholeDesc wholeDesc) {
        return parser.parseByDesc(wholeDesc);
    }

    @Override
    public Object exec(Method proxyMethod, Object[] args) {
        try (Connection connection = dataSource.getConnection()) {
            this.exec(connection, proxyMethod, args);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description 用于代理方法中调用的执行
     *
     * @author zhaolaiyuan
     * Date 2022/9/6 8:46
     **/
    public Object exec(Connection connection, Method proxyMethod, Object[] args) {
        SQLBlueprint sqlBlueprint = (SQLBlueprint) mapperMethod_FinalDefinition_Map.get(proxyMethod);
        if (sqlBlueprint == null) {
            // 没有预先定义方法
            throw ParseException.undefinedMethod();
        }
        ParamManager paramManager = paramManagerFactory.getByProxyMethod(sqlBlueprint, proxyMethod, args);
        // 执行
        ExecutePlan executePlan = this.createExecutePlan(sqlBlueprint.getFlag(), proxyMethod.getReturnType(), connection);
        return runningPipeline.go(sqlBlueprint, paramManager, executePlan);
    }

    protected ExecutePlan createExecutePlan(RivuletFlag rivuletFlag, Class<?> returnType, Connection connection) {
        ExecutePlan executePlan = rivuletFlagClassExecutePlanMap.get(rivuletFlag, returnType);
        if (executePlan != null) {
            return executePlan;
        }
        if (RivuletFlag.QUERY.equals(rivuletFlag)) {
            if (ClassUtils.isExtend(Collection.class, returnType)) {
                Collection<Object> collection = super.collectionInstanceCreator.create(returnType);
                return new SQLQueryManyExecutePlan(connection, collection);
            } else {
                return new SQLQueryOneExecutePlan(connection);
            }
//            } else if (RivuletFlag.INSERT.equals(sqlBlueprint.getFlag()) && paramManager instanceof ModelBatchParamManager) {
//                // 批量插入
//                MySQLRivuletProperties rivuletProperties = this.getRivuletProperties();
//                executePlan = new MySQLBatchInsertExecutePlan(rivuletProperties, connection);
        } else {
            return new SQLUpdateOneExecutePlan(connection);
        }
    }


    public void registerExecutePlan(RivuletFlag rivuletFlag, Class<?> returnType, ExecutePlan executePlan) {
        this.rivuletFlagClassExecutePlanMap.put(rivuletFlag, returnType, executePlan);
    }

    /**
     * 通过预定义desc的key，查询单个
     **/
    @Override
    public <T> T queryOneByDescKey(String descKey, Map<String, Object> params) {
        Blueprint blueprint = parser.parseByKey(descKey);
        if (blueprint instanceof SqlQueryDefinition) {
            return this.queryOneByBlueprint(blueprint, params);
        } else {
            throw ExecuteException.execError("执行类型不匹配，此处仅支持执行查询方法");
        }
    }

    @Override
    public <T> T queryOneByBlueprint(Blueprint blueprint, ParamManager paramManager) {
        try (Connection connection = dataSource.getConnection()) {
            return this.queryOneByBlueprint(connection, blueprint, paramManager);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T queryOneByBlueprint(Connection connection, Blueprint blueprint, ParamManager paramManager) {
        if (blueprint instanceof SqlQueryDefinition) {
            // 单个查询
            SQLQueryOneExecutePlan sqlQueryOneExecutePlan = new SQLQueryOneExecutePlan(connection);
            return runningPipeline.go(blueprint, paramManager, sqlQueryOneExecutePlan);
        } else {
            throw ExecuteException.execError("执行类型不匹配，此处仅支持执行sql类查询方法");
        }
    }

    @Override
    public <T, I> T queryById(I id, Class<T> modelClass) {
        ModelMeta modelMeta = definer.createOrGetModelMeta(modelClass);
        Blueprint blueprint = this.parser.parseSelectByMeta(modelMeta);
        return this.queryOneByBlueprint(blueprint, Collections.singletonMap(Constant.MAIN_ID, id));
    }

    @Override
    public <T> void queryManyByDescKey(String descKey, Map<String, Object> params, Collection<T> resultContainer) {
        Blueprint blueprint = parser.parseByKey(descKey);
        this.queryManyByBlueprint(blueprint, params, resultContainer);
    }

    @Override
    public <T> void queryManyByBlueprint(Blueprint blueprint, ParamManager paramManager, Collection<T> resultContainer) {
        if (blueprint instanceof SqlQueryDefinition) {
            Connection connection = this.getConnection();
            try {
                // 批量查询
                SQLQueryManyExecutePlan executePlan = new SQLQueryManyExecutePlan(connection, (Collection<Object>) resultContainer);
                this.runningPipeline.go(blueprint, paramManager, executePlan);
            } finally {
                this.closeConnection(connection);
            }
        } else {
            throw ExecuteException.execError("执行类型不匹配，此处仅支持执行sql类查询方法");
        }
    }

    @Override
    public <T, I> List<T> queryByIds(Collection<I> ids, Class<T> modelClass) {
        ModelMeta modelMeta = definer.createOrGetModelMeta(modelClass);
        Blueprint blueprint = this.parser.parseSelectByMeta(modelMeta);
        return this.queryManyByBlueprint(blueprint, Collections.singletonMap(Constant.MAIN_IDS, ids));
    }

    @Override
    public <T> int insertOne(T obj) {
        Class<?> clazz = obj.getClass();
        ModelMeta modelMeta = definer.createOrGetModelMeta(clazz);
        SQLBlueprint blueprint = (SQLBlueprint) parser.parseInsertByMeta(modelMeta);

        ParamManager paramManager = paramManagerFactory.getByModelMeta(modelMeta, obj);
        Connection connection = this.getConnection();
        try {
            SQLUpdateOneExecutePlan executePlan = new SQLUpdateOneExecutePlan(connection);
            return runningPipeline.go(blueprint, paramManager, executePlan);
        } finally {
            this.closeConnection(connection);
        }
    }

}
