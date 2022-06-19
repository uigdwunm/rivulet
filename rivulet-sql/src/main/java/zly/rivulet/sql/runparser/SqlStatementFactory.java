package zly.rivulet.sql.runparser;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.utils.RelationSwitch;
import zly.rivulet.sql.runparser.statement.SqlStatement;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlStatementFactory {
    /**
     * 初始化statement生成器，一个语句必须先执行初始化才能执行运行时statement
     **/
    private final Map<Class<? extends Definition>, StatementInitCreator> DEFINITION_INIT_CREATOR_MAP = new ConcurrentHashMap<>();

    /**
     * 运行时statement生成器，运行时主要走这里的
     **/
    private final Map<Class<? extends Definition>, StatementRunCreator> DEFINITION_RUN_CREATOR_MAP = new ConcurrentHashMap<>();

    /**
     * definition类和statement之间的缓存映射
     **/
    private final Map<Definition, SqlStatement> statementCache = new ConcurrentHashMap<>();

    public void register(Class<? extends Definition> definitionClass, StatementInitCreator statementInitCreator, StatementRunCreator statementRunCreator) {
        DEFINITION_INIT_CREATOR_MAP.put(definitionClass, statementInitCreator);
        DEFINITION_RUN_CREATOR_MAP.put(definitionClass, statementRunCreator);
    }

    public SqlStatement getOrCreate(Definition definition, SqlRunParseHelper sqlRunParseHelper) {
        SqlStatement statement = statementCache.get(definition);
        if (statement == null) {
            StatementRunCreator statementRunCreator = DEFINITION_RUN_CREATOR_MAP.get(definition.getClass());
            statement = statementRunCreator.create(definition, sqlRunParseHelper);
        }
        return statement;
    }

    public SqlStatement init(Definition definition, RelationSwitch soleFlag, SqlRunParseInitHelper initHelper) {
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
        SqlStatement create(Definition definition, RelationSwitch soleFlag, SqlRunParseInitHelper helper);
    }

    @FunctionalInterface
    public interface StatementRunCreator {
        SqlStatement create(Definition definition, SqlRunParseHelper helper);
    }
}
