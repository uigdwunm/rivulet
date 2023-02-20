package zly.rivulet.sql.generator;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.utils.RelationSwitch;
import zly.rivulet.sql.generator.statement.SQLStatement;
import zly.rivulet.sql.generator.toolbox.SQLGenerateToolbox;
import zly.rivulet.sql.generator.toolbox.WarmUpToolbox;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SQLStatementFactory {
    /**
     * 初始化statement生成器，一个语句必须先执行初始化才能执行运行时statement
     **/
    private final Map<Class<?>, StatementInitCreator> DEFINITION_WARM_UP_CREATOR_MAP = new ConcurrentHashMap<>();

    /**
     * 运行时statement生成器，运行时主要走这里的
     **/
    private final Map<Class<?>, StatementRunCreator> DEFINITION_RUN_CREATOR_MAP = new ConcurrentHashMap<>();


    public void register(Class<?> clazz, StatementInitCreator statementInitCreator, StatementRunCreator statementRunCreator) {
        DEFINITION_WARM_UP_CREATOR_MAP.put(clazz, statementInitCreator);
        DEFINITION_RUN_CREATOR_MAP.put(clazz, statementRunCreator);
    }

    public SQLStatement getOrCreate(Definition definition, SQLGenerateToolbox toolbox) {
        Definition replaceDefinition = toolbox.getReplaceDefinition(definition.getClass());
        if (replaceDefinition != null) {
            definition = replaceDefinition;
        } else {
            // 如果存在replaceDefinition，那一定没有缓存，不存在才需要查下
            SQLStatement statement = toolbox.getBlueprint().getStatement(definition);
            if (statement != null) {
                // 缓存查到了
                return statement;
            }
        }
        StatementRunCreator statementRunCreator = DEFINITION_RUN_CREATOR_MAP.get(definition.getClass());
        return statementRunCreator.create(definition, toolbox);
    }

    public SQLStatement warmUp(Definition definition, RelationSwitch soleFlag, WarmUpToolbox toolbox) {
        StatementInitCreator statementInitCreator = DEFINITION_WARM_UP_CREATOR_MAP.get(definition.getClass());
        SQLStatement sqlStatement = statementInitCreator.create(definition, soleFlag, toolbox);
        if (soleFlag.isEnable()) {
            // 唯一，可以缓存
            toolbox.getSqlBlueprint().putStatement(definition, sqlStatement);
            // 生成缓存
            sqlStatement.initCache();
        }

        return sqlStatement;
    }


    @FunctionalInterface
    public interface StatementInitCreator {
        SQLStatement create(Object definition, RelationSwitch soleFlag, WarmUpToolbox helper);
    }

    @FunctionalInterface
    public interface StatementRunCreator {
        SQLStatement create(Object definition, SQLGenerateToolbox helper);
    }
}
