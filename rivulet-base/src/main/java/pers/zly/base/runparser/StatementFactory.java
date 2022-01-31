package pers.zly.base.runparser;

import pers.zly.base.definition.AbstractDefinition;
import pers.zly.base.definition.Definition;
import pers.zly.base.runparser.statement.AbstractStatement;
import pers.zly.base.runparser.statement.Statement;
import pers.zly.base.utils.RelationSwitch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatementFactory {
    /**
     * key是 Definition的class
     * TODO value是abstractStatement的 反射构造函数，传一个Definition的那种
     **/
    private final Map<Class<Definition>, AbstractStatement> DEFINITION_CONSTRUCTOR_MAP = new ConcurrentHashMap<>();

    /**
     * definition类的class对象和statement之间的缓存映射
     **/
    private final Map<Class<?>, AbstractStatement> statementCache = new ConcurrentHashMap<>();

    public boolean register(Class<Definition> definitionClass, AbstractStatement abstractStatement) {
        // TODO
        return true;
    }

    public AbstractStatement getOrCreate(AbstractDefinition definition, ParamManager paramManager) {
        AbstractStatement statement = statementCache.get(definition.getClass());
        if (statement == null) {
            // TODO 改成构造函数
            statement = DEFINITION_CONSTRUCTOR_MAP.get(definition.getClass());
            // constructor(definition, args, this)
        }
        return statement;
    }

    public AbstractStatement init(AbstractDefinition definition, RelationSwitch relationSwitch) {
        // TODO 改成构造函数
        AbstractStatement statement = DEFINITION_CONSTRUCTOR_MAP.get(definition.getClass());
        // constructor(definition, rootSwitch, this)

        if (definition.canCatchStatement()) {
            statementCache.put(definition.getClass(), statement);
        }

        return statement;
    }
}
