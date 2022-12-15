package zly.rivulet.sql;

import zly.rivulet.base.DefaultOperation;
import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.ExecuteException;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.SimpleParamManager;
import zly.rivulet.base.pipeline.ExecutePlan;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.sql.convertor.SQLDefaultConvertor;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.pipeline.SQLQueryManyExecutePlan;
import zly.rivulet.sql.pipeline.SQLQueryOneExecutePlan;
import zly.rivulet.sql.pipeline.SQLUpdateOneExecutePlan;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

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

        // 注册默认的转换器
        SQLDefaultConvertor.registerDefault(generator.getConvertorManager());
    }

    @Override
    public Blueprint parse(WholeDesc wholeDesc) {
        return parser.parseByDesc(wholeDesc);
    }

    @Override
    public Object exec(Method proxyMethod, Object[] args) {
        try (Connection connection = dataSource.getConnection()) {
            return this.exec(connection, proxyMethod, args);
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

    public <T> T queryOneByDescKey(Connection connection, String descKey, Map<String, Object> params) {
        Blueprint blueprint = parser.parseByKey(descKey);
        if (blueprint instanceof SqlQueryDefinition) {
            return this.queryOneByBlueprint(connection, blueprint, params);
        } else {
            throw ExecuteException.execError("执行类型不匹配，此处仅支持执行查询方法");
        }
    }

    public  <T> T queryOneByBlueprint(Connection connection, Blueprint blueprint, Map<String, Object> params) {
        return this.queryOneByBlueprint(connection, blueprint, new SimpleParamManager(params));
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

    public <T, I> T queryById(Connection connection, I id, Class<T> modelClass) {
        ModelMeta modelMeta = definer.createOrGetModelMeta(modelClass);
        Blueprint blueprint = this.parser.parseSelectByMeta(modelMeta);
        return this.queryOneByBlueprint(connection, blueprint, Collections.singletonMap(Constant.MAIN_ID, id));
    }

    @Override
    public <T> void queryManyByDescKey(String descKey, Map<String, Object> params, Collection<T> resultContainer) {
        Blueprint blueprint = parser.parseByKey(descKey);
        this.queryManyByBlueprint(blueprint, params, resultContainer);
    }

    public <T> void queryManyByDescKey(Connection connection, String descKey, Map<String, Object> params, Collection<T> resultContainer) {
        Blueprint blueprint = parser.parseByKey(descKey);
        this.queryManyByBlueprint(connection, blueprint, params, resultContainer);
    }

    public  <T> void queryManyByBlueprint(Connection connection, Blueprint blueprint, Map<String, Object> params, Collection<T> resultContainer) {
        this.queryManyByBlueprint(connection, blueprint, new SimpleParamManager(params), resultContainer);
    }


    @Override
    public <T> void queryManyByBlueprint(Blueprint blueprint, ParamManager paramManager, Collection<T> resultContainer) {
        try (Connection connection = dataSource.getConnection()) {
            // 批量查询
            this.queryManyByBlueprint(connection, blueprint, paramManager, resultContainer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void queryManyByBlueprint(Connection connection, Blueprint blueprint, ParamManager paramManager, Collection<T> resultContainer) {
        if (blueprint instanceof SqlQueryDefinition) {
            // 批量查询
            SQLQueryManyExecutePlan executePlan = new SQLQueryManyExecutePlan(connection, (Collection<Object>) resultContainer);
            this.runningPipeline.go(blueprint, paramManager, executePlan);
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

    public <T, I> List<T> queryByIds(Connection connection, Collection<I> ids, Class<T> modelClass) {
        ModelMeta modelMeta = definer.createOrGetModelMeta(modelClass);
        Blueprint blueprint = this.parser.parseSelectByMeta(modelMeta);
        return this.queryManyByBlueprint(connection, blueprint, Collections.singletonMap(Constant.MAIN_IDS, ids));
    }

    public <T> List<T> queryManyByBlueprint(Connection connection, Blueprint blueprint, Map<String, Object> params) {
        List<T> list = new LinkedList<>();
        this.queryManyByBlueprint(connection, blueprint, params, list);
        return list;
    }

    @Override
    public <T> int insertOne(T obj) {
        try (Connection connection = dataSource.getConnection()) {
            return this.insertOne(connection, obj);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> int insertOne(Connection connection, T obj) {
        Class<?> clazz = obj.getClass();
        ModelMeta modelMeta = definer.createOrGetModelMeta(clazz);
        SQLBlueprint blueprint = (SQLBlueprint) parser.parseInsertByMeta(modelMeta);

        ParamManager paramManager = paramManagerFactory.getByModelMeta(modelMeta, obj);
        SQLUpdateOneExecutePlan executePlan = new SQLUpdateOneExecutePlan(connection);
        return runningPipeline.go(blueprint, paramManager, executePlan);
    }

    @Override
    public <T> List<Integer> batchInsert(Collection<T> batchModel, Class<T> dOModelClass) {
        try (Connection connection = dataSource.getConnection()) {
            return this.batchInsert(connection, batchModel, dOModelClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract <T> List<Integer> batchInsert(Connection connection, Collection<T> batchModel, Class<T> dOModelClass);

    @Override
    public <T> int updateOneById(T obj) {
        try (Connection connection = dataSource.getConnection()) {
            return this.updateOneById(connection, obj);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public  <T> int updateOneById(Connection connection, T obj) {
        Class<?> clazz = obj.getClass();
        ModelMeta modelMeta = definer.createOrGetModelMeta(clazz);
        SQLBlueprint blueprint = (SQLBlueprint) parser.parseUpdateByMeta(modelMeta);
        ParamManager paramManager = paramManagerFactory.getByModelMeta(modelMeta, obj);
        SQLUpdateOneExecutePlan executePlan = new SQLUpdateOneExecutePlan(connection);
        return runningPipeline.go(blueprint, paramManager, executePlan);
    }

    @Override
    public <T> int batchUpdateById(Collection<T> obj, Class<T> dOModelClass) {
        try (Connection connection = dataSource.getConnection()) {
            return this.batchUpdateById(connection, obj, dOModelClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract <T> int batchUpdateById(Connection connection, Collection<T> obj, Class<T> dOModelClass);

    @Override
    public int updateByDescKey(String descKey, Map<String, Object> params) {
        try (Connection connection = dataSource.getConnection()) {
            return this.updateByDescKey(connection, descKey, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateByDescKey(Connection connection, String descKey, Map<String, Object> params) {
        Blueprint blueprint = parser.parseByKey(descKey);
        return this.updateByBlueprint(connection, blueprint, params);
    }

    @Override
    public int updateByBlueprint(Blueprint blueprint, Map<String, Object> params) {
        try (Connection connection = dataSource.getConnection()) {
            return this.updateByBlueprint(connection, blueprint, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateByBlueprint(Connection connection, Blueprint blueprint, Map<String, Object> params) {
        SimpleParamManager simpleParamManager = new SimpleParamManager(params);
        SQLUpdateOneExecutePlan executePlan = new SQLUpdateOneExecutePlan(connection);
        return runningPipeline.go(blueprint, simpleParamManager, executePlan);
    }

    @Override
    public <T, I> int deleteById(I id, Class<T> modelClass) {
        try (Connection connection = dataSource.getConnection()) {
            return this.deleteById(connection, id, modelClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <I, T> int deleteById(Connection connection, I id, Class<T> modelClass) {
        ModelMeta modelMeta = definer.createOrGetModelMeta(modelClass);
        SQLBlueprint blueprint = (SQLBlueprint) parser.parseDeleteByMeta(modelMeta);
        return this.deleteByBlueprint(connection, blueprint, Collections.singletonMap(Constant.MAIN_ID, id));
    }

    @Override
    public <T, I> int deleteByIds(Collection<I> ids, Class<T> modelClass) {
        try (Connection connection = dataSource.getConnection()) {
            return this.deleteByIds(connection, ids, modelClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <I, T> int deleteByIds(Connection connection, Collection<I> ids, Class<T> modelClass) {
        ModelMeta modelMeta = definer.createOrGetModelMeta(modelClass);
        SQLBlueprint blueprint = (SQLBlueprint) parser.parseDeleteByMeta(modelMeta);
        return this.deleteByBlueprint(connection, blueprint, Collections.singletonMap(Constant.MAIN_IDS, ids));
    }

    @Override
    public int deleteByBlueprint(Blueprint blueprint, Map<String, Object> params) {
        try (Connection connection = dataSource.getConnection()) {
            return this.deleteByBlueprint(connection, blueprint, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteByBlueprint(Connection connection, Blueprint blueprint, Map<String, Object> params) {
        SQLUpdateOneExecutePlan executePlan = new SQLUpdateOneExecutePlan(connection);
        return runningPipeline.go(blueprint, new SimpleParamManager(params), executePlan);
    }


    public class Transaction implements DefaultOperation {
        private final Connection connection;

        private final SQLRivuletManager sqlRivuletManager;

        private volatile boolean isClosed = false;

        private Transaction(Connection connection, SQLRivuletManager sqlRivuletManager) {
            this.connection = connection;
            this.sqlRivuletManager = sqlRivuletManager;
            try {
                // 关闭自动提交
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void commit() {
            try {
                connection.commit();
                this.isClosed = true;
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void rollback() {
            try {
                connection.rollback();
                this.isClosed = true;
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private Connection getConnection() {
            if (isClosed) {
                throw new IllegalStateException("事物已提交, 无法再操作");
            }
            return this.connection;
        }

        @Override
        public Blueprint parse(WholeDesc wholeDesc) {
            return sqlRivuletManager.parse(wholeDesc);
        }

        @Override
        public <T> T queryOneByDescKey(String descKey, Map<String, Object> params) {
            return sqlRivuletManager.queryOneByDescKey(this.getConnection(), descKey, params);
        }

        @Override
        public <T> T queryOneByBlueprint(Blueprint blueprint, ParamManager paramManager) {
            return sqlRivuletManager.queryOneByBlueprint(this.getConnection(), blueprint, paramManager);
        }

        @Override
        public <T, I> T queryById(I id, Class<T> modelClass) {
            return sqlRivuletManager.queryById(this.getConnection(), id, modelClass);
        }

        @Override
        public <T> void queryManyByDescKey(String descKey, Map<String, Object> params, Collection<T> resultContainer) {
            sqlRivuletManager.queryManyByDescKey(this.getConnection(), descKey, params, resultContainer);

        }

        @Override
        public <T> void queryManyByBlueprint(Blueprint blueprint, ParamManager paramManager, Collection<T> resultContainer) {
            sqlRivuletManager.queryManyByBlueprint(this.getConnection(), blueprint, paramManager, resultContainer);
        }

        @Override
        public <T, I> List<T> queryByIds(Collection<I> ids, Class<T> modelClass) {
            return sqlRivuletManager.queryByIds(this.getConnection(), ids, modelClass);
        }

        public <T> int insertOne(T obj) {
            return sqlRivuletManager.insertOne(this.getConnection(), obj);
        }

        @Override
        public <T> List<Integer> batchInsert(Collection<T> batchModel, Class<T> dOModelClass) {
            return sqlRivuletManager.batchInsert(this.getConnection(), batchModel, dOModelClass);
        }

        @Override
        public <T> int updateOneById(T obj) {
            return sqlRivuletManager.updateOneById(this.getConnection(), obj);
        }

        @Override
        public <T> int batchUpdateById(Collection<T> obj, Class<T> dOModelClass) {
            return sqlRivuletManager.batchUpdateById(this.getConnection(), obj, dOModelClass);
        }

        @Override
        public int updateByDescKey(String descKey, Map<String, Object> params) {
            return sqlRivuletManager.updateByDescKey(this.getConnection(), descKey, params);
        }

        @Override
        public int updateByBlueprint(Blueprint blueprint, Map<String, Object> params) {
            return sqlRivuletManager.updateByBlueprint(this.getConnection(), blueprint, params);
        }

        @Override
        public <T, I> int deleteById(I id, Class<T> modelClass) {
            return sqlRivuletManager.deleteById(this.getConnection(), id, modelClass);
        }

        @Override
        public <T, I> int deleteByIds(Collection<I> ids, Class<T> modelClass) {
            return sqlRivuletManager.deleteByIds(this.getConnection(), ids, modelClass);
        }

        @Override
        public int deleteByBlueprint(Blueprint blueprint, Map<String, Object> params) {
            return sqlRivuletManager.deleteByBlueprint(this.getConnection(), blueprint, params);
        }

    }
}
