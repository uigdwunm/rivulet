package zly.rivulet.sql.runparser;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.utils.RelationSwitch;
import zly.rivulet.sql.runparser.statement.AbstractSqlStatement;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlStatementFactory {
    /**
     * key是 Definition的class
     **/
    private final Map<Class<? extends AbstractDefinition>, StatementInitCreator> DEFINITION_INIT_CREATOR_MAP = new HashMap<>();

    private final Map<Class<? extends AbstractDefinition>, StatementRunCreator> DEFINITION_RUN_CREATOR_MAP = new ConcurrentHashMap<>();

    /**
     * definition类和statement之间的缓存映射
     **/
    private final Map<AbstractDefinition, AbstractSqlStatement> statementCache = new ConcurrentHashMap<>();

    public void register(Class<? extends AbstractDefinition> definitionClass, StatementInitCreator statementInitCreator, StatementRunCreator statementRunCreator) {
        DEFINITION_INIT_CREATOR_MAP.put(definitionClass, statementInitCreator);
        DEFINITION_RUN_CREATOR_MAP.put(definitionClass, statementRunCreator);
    }

    public AbstractSqlStatement getOrCreate(AbstractDefinition definition, ParamManager paramManager) {
        AbstractSqlStatement statement = statementCache.get(definition);
        if (statement == null) {
            StatementRunCreator statementRunCreator = DEFINITION_RUN_CREATOR_MAP.get(definition.getClass());
            statement = statementRunCreator.create(definition, paramManager, this);
        }
        return statement;
    }

    public AbstractSqlStatement init(AbstractDefinition definition, RelationSwitch soleFlag) {
        StatementInitCreator statementInitCreator = DEFINITION_INIT_CREATOR_MAP.get(definition.getClass());
        return statementInitCreator.create(definition, soleFlag, this);
    }

    /**
     * Description 缓存唯一性的
     *
     * @author zhaolaiyuan
     * Date 2022/2/5 12:51
     **/
    protected void catchSole(AbstractDefinition definition, AbstractSqlStatement statement) {
        statementCache.put(definition, statement);
    }


    @FunctionalInterface
    public interface StatementInitCreator {

        AbstractSqlStatement create(Definition definition, RelationSwitch soleFlag, SqlStatementFactory statementFactory);
    }

    @FunctionalInterface
    public interface StatementRunCreator {

        AbstractSqlStatement create(Definition definition, ParamManager paramManager, SqlStatementFactory statementFactory);
    }
}
