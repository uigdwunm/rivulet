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
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.FixedLengthStatementCollector;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.sql.assigner.SQLQueryResultAssigner;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.generator.SQLFish;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class SQLRivuletManager extends RivuletManager {
    private final DataSource dataSource;

    protected SQLRivuletManager(
        Generator generator,
        WarehouseManager warehouseManager,
        DataSource dataSource
    ) {
        super(generator.getParser(), generator, generator.getProperties(), generator.getConvertorManager(), warehouseManager);
        this.dataSource = dataSource;
    }

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public Blueprint parse(WholeDesc wholeDesc) {
        return parser.parseByDesc(wholeDesc);
    }

    /**
     * Description 用于代理方法中调用的执行
     *
     * @author zhaolaiyuan
     * Date 2022/9/6 8:46
     **/
    @Override
    public Object exec(Method proxyMethod, Object[] args) {
        SQLBlueprint sqlBlueprint = (SQLBlueprint) mapperMethod_FinalDefinition_Map.get(proxyMethod);
        if (sqlBlueprint == null) {
            // 没有预先定义方法
            throw ParseException.undefinedMethod();
        }
        ParamManager paramManager = paramManagerFactory.getByProxyMethod(sqlBlueprint, proxyMethod, args);
        // 判断是，返回单值还是list，传入不同的executor
        Class<?> returnType = proxyMethod.getReturnType();
        try (Connection connection = this.getConnection()) {
            if (RivuletFlag.QUERY.equals(sqlBlueprint.getFlag())) {
                if (ClassUtils.isExtend(Collection.class, returnType)) {
                    // 结果是集合类型
                    // 推断并创建集合容器
                    Collection<Object> collection = collectionInstanceCreator.create(returnType);
                    return this.executeQueryMany(connection, sqlBlueprint, paramManager, collection);
                } else {
                    // 单个结果
                    return this.executeQueryOne(connection, sqlBlueprint, paramManager);
                }
            } else {
                // 增删改操作
                return this.executeUpdate(connection, sqlBlueprint, paramManager);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
        if (blueprint instanceof SqlQueryDefinition) {
            try (Connection connection = this.getConnection()) {
                // 单个查询
                return (T) this.executeQueryOne(connection, (SQLBlueprint) blueprint, paramManager);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
            try (Connection connection = this.getConnection()) {
                // 批量查询
                this.executeQueryMany(connection, (SQLBlueprint) blueprint, paramManager, (Collection<Object>) resultContainer);
            } catch (SQLException e) {
                throw new RuntimeException(e);
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

        ParamManager paramManager = paramManagerFactory.getByModelMeta(blueprint, modelMeta, obj);
        try (Connection connection = this.getConnection()) {
            return this.executeUpdate(connection, blueprint, paramManager);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Object executeQueryOne(Connection connection, SQLBlueprint sqlBlueprint, ParamManager paramManager) {
        return runningPipeline.go(sqlBlueprint, paramManager, fish -> {
            SQLFish sqlFish = (SQLFish) fish;
            StatementCollector collector = new FixedLengthStatementCollector(sqlFish.getLength());
            sqlFish.getStatement().collectStatement(collector);
            try {
                PreparedStatement statement = connection.prepareStatement(collector.toString());
                ResultSet resultSet = statement.executeQuery(collector.toString());
                SQLQueryResultAssigner assigner = (SQLQueryResultAssigner) sqlBlueprint.getAssigner();
                return assigner.getValue(resultSet, 0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Object executeQueryMany(Connection connection, SQLBlueprint sqlBlueprint, ParamManager paramManager, Collection<Object> collection) {
        return runningPipeline.go(sqlBlueprint, paramManager, fish -> {
            SQLFish sqlFish = (SQLFish) fish;
            StatementCollector collector = new FixedLengthStatementCollector(sqlFish.getLength());
            sqlFish.getStatement().collectStatement(collector);
            try {
                PreparedStatement statement = connection.prepareStatement(collector.toString());
                SQLQueryResultAssigner assigner = (SQLQueryResultAssigner) sqlBlueprint.getAssigner();

                // 执行查询
                ResultSet resultSet = statement.executeQuery(collector.toString());

                // 拼装结果
                while (resultSet.next()) {
                    collection.add(assigner.getValue(resultSet, 0));
                }
                // 回收资源
                resultSet.close();

                return collection;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private int executeUpdate(Connection connection, SQLBlueprint sqlBlueprint, ParamManager paramManager) {
        return (int) runningPipeline.go(sqlBlueprint, paramManager, fish -> {
            SQLFish sqlFish = (SQLFish) fish;
            StatementCollector collector = new FixedLengthStatementCollector(sqlFish.getLength());
            sqlFish.getStatement().collectStatement(collector);
            try {
                PreparedStatement statement = connection.prepareStatement(collector.toString());
                return statement.executeUpdate(collector.toString());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected int[] executeBatchUpdate(Connection connection, SQLBlueprint sqlBlueprint, ParamManager paramManager, boolean isLast) {
        return (int[]) runningPipeline.go(sqlBlueprint, paramManager, fish -> {
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
        });
    }

    public class Transaction implements DefaultOperation {
        private final Connection connection;

        private volatile boolean isClosed = false;

        private Transaction(Connection connection) {
            this.connection = connection;
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

        @Override
        public Blueprint parse(WholeDesc wholeDesc) {
            return null;
        }

        @Override
        public <T> T queryOneByDescKey(String descKey, Map<String, Object> params) {
            return null;
        }

        @Override
        public <T> T queryOneByBlueprint(Blueprint blueprint, ParamManager paramManager) {
            return null;
        }

        @Override
        public <T, I> T queryById(I id, Class<T> modelClass) {
            return null;
        }

        @Override
        public <T> void queryManyByDescKey(String descKey, Map<String, Object> params, Collection<T> resultContainer) {

        }

        @Override
        public <T> void queryManyByBlueprint(Blueprint blueprint, ParamManager paramManager, Collection<T> resultContainer) {

        }

        @Override
        public <T, I> List<T> queryByIds(Collection<I> ids, Class<T> modelClass) {
            return null;
        }

        public <T> int insertOne(T obj) {
            if (isClosed) {
                throw new IllegalStateException("事物已提交, 无法再操作");
            }
            Class<?> clazz = obj.getClass();
            ModelMeta modelMeta = definer.createOrGetModelMeta(clazz);
            SQLBlueprint blueprint = (SQLBlueprint) parser.parseInsertByMeta(modelMeta);

            ParamManager paramManager = paramManagerFactory.getByModelMeta(blueprint, modelMeta, new Object[]{obj});
            return executeUpdate(connection, blueprint, paramManager);
        }

        @Override
        public <T> int updateOneById(T obj) {
            return 0;
        }

        @Override
        public <T> int batchUpdateById(Collection<T> obj) {
            return 0;
        }

        @Override
        public int updateByDescKey(String descKey, Map<String, Object> params) {
            return 0;
        }

        @Override
        public int updateByBlueprint(Blueprint blueprint, Map<String, Object> params) {
            return 0;
        }

        @Override
        public <T, I> int deleteById(I id, Class<T> modelClass) {
            return 0;
        }

        @Override
        public <T, I> int deleteByIds(Collection<I> ids, Class<T> modelClass) {
            return 0;
        }

        @Override
        public int deleteByBlueprint(Blueprint blueprint, Map<String, Object> params) {
            return 0;
        }

    }
}
