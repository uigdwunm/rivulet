package zly.rivulet.sql.generator;

import zly.rivulet.base.utils.RelationSwitch;
import zly.rivulet.sql.generator.statement.SqlStatement;
import zly.rivulet.sql.generator.toolbox.GenerateToolbox;
import zly.rivulet.sql.generator.toolbox.WarmUpToolbox;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlStatementFactory {
    /**
     * 初始化statement生成器，一个语句必须先执行初始化才能执行运行时statement
     **/
    private final Map<Class<?>, StatementInitCreator> DEFINITION_INIT_CREATOR_MAP = new ConcurrentHashMap<>();

    /**
     * 运行时statement生成器，运行时主要走这里的
     **/
    private final Map<Class<?>, StatementRunCreator> DEFINITION_RUN_CREATOR_MAP = new ConcurrentHashMap<>();

    /**
     * definition类和statement之间的缓存映射
     **/
    private final Map<Object, SqlStatement> statementCache = new ConcurrentHashMap<>();

    public void register(Class<?> clazz, StatementInitCreator statementInitCreator, StatementRunCreator statementRunCreator) {
        DEFINITION_INIT_CREATOR_MAP.put(clazz, statementInitCreator);
        DEFINITION_RUN_CREATOR_MAP.put(clazz, statementRunCreator);
    }

    public SqlStatement getOrCreate(Object definition, GenerateToolbox generateToolbox) {
        SqlStatement statement = statementCache.get(definition);
        if (statement == null) {
            StatementRunCreator statementRunCreator = DEFINITION_RUN_CREATOR_MAP.get(definition.getClass());
            statement = statementRunCreator.create(definition, generateToolbox);
        }
        return statement;
    }

    public SqlStatement init(Object definition, RelationSwitch soleFlag, WarmUpToolbox initHelper) {
        StatementInitCreator statementInitCreator = DEFINITION_INIT_CREATOR_MAP.get(definition.getClass());
        SqlStatement sqlStatement = statementInitCreator.create(definition, soleFlag, initHelper);
        if (soleFlag.isEnable()) {
            // 唯一，可以缓存
            statementCache.put(definition, sqlStatement);
        }

        return sqlStatement;
    }


    @FunctionalInterface
    public interface StatementInitCreator {
        SqlStatement create(Object definition, RelationSwitch soleFlag, WarmUpToolbox helper);
    }

    @FunctionalInterface
    public interface StatementRunCreator {
        SqlStatement create(Object definition, GenerateToolbox helper);
    }
}
