package zly.rivulet.sql;

import zly.rivulet.base.DefaultOperation;
import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.RivuletProperties;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.parser.Parser;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.collector.FixedLengthStatementCollector;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.sql.assigner.SQLQueryResultAssigner;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.generator.SQLFish;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class SQLRivuletManager extends RivuletManager {
    private final DataSource dataSource;

    protected SQLRivuletManager(
        Parser parser,
        Generator generator,
        RivuletProperties configProperties,
        ConvertorManager convertorManager,
        WarehouseManager warehouseManager,
        DataSource dataSource
    ) {
        super(parser, generator, configProperties, convertorManager, warehouseManager);
        this.dataSource = dataSource;
    }

    protected Connection getAutoCommitConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);
        return connection;
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
        try (Connection connection = this.getAutoCommitConnection()) {
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

    @Override
    public <T> T queryOneByDesc(WholeDesc wholeDesc, ParamManager paramManager) {
        SQLBlueprint sqlBlueprint = (SQLBlueprint) parser.parseByDesc(wholeDesc);
        try (Connection connection = this.getAutoCommitConnection()) {
            // 单个查询
            return (T) this.executeQueryOne(connection, sqlBlueprint, paramManager);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void queryManyByDesc(WholeDesc wholeDesc, ParamManager paramManager, Collection<T> resultContainer) {
        SQLBlueprint sqlBlueprint = (SQLBlueprint) parser.parseByDesc(wholeDesc);
        try (Connection connection = this.getAutoCommitConnection()) {
            // 批量查询
            this.executeQueryMany(connection, sqlBlueprint, paramManager, (Collection<Object>) resultContainer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> int insertOne(T obj) {
        Class<?> clazz = obj.getClass();
        ModelMeta modelMeta = definer.createOrGetModelMeta(clazz);
        SQLBlueprint blueprint = (SQLBlueprint) parser.parseInsertByMeta(modelMeta);

        ParamManager paramManager = paramManagerFactory.getByModelMeta(modelMeta, new Object[]{obj});
        try (Connection connection = this.getAutoCommitConnection()) {
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
                return assigner.assign(resultSet);
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
                    collection.add(assigner.assign(resultSet));
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
        public <T> T queryOneByDesc(WholeDesc wholeDesc, ParamManager paramManager) {
            return null;
        }

        @Override
        public <T> void queryManyByDesc(WholeDesc wholeDesc, ParamManager paramManager, Collection<T> resultContainer) {

        }

        @Override
        public <T, I> T queryOneById(I id, Class<T> modelClass) {
            return null;
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

            ParamManager paramManager = paramManagerFactory.getByModelMeta(modelMeta, new Object[]{obj});
            return executeUpdate(connection, blueprint, paramManager);
        }

        @Override
        public <T> int batchInsert(Collection<T> batchModel) {
            return 0;
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
        public int updateByDesc(WholeDesc wholeDesc, Map<String, Object> params) {
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
        public int deleteByDesc(WholeDesc wholeDesc, Map<String, Object> params) {
            return 0;
        }

    }
}
