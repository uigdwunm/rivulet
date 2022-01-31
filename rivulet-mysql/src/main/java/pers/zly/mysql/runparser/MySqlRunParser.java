package pers.zly.mysql.runparser;

import pers.zly.base.definition.AbstractDefinition;
import pers.zly.base.runparser.ParamManager;
import pers.zly.base.runparser.RuntimeParser;
import pers.zly.base.runparser.StatementFactory;
import pers.zly.base.runparser.statement.Statement;
import pers.zly.base.utils.RelationSwitch;

public class MySqlRunParser implements RuntimeParser {


    private final StatementFactory statementFactory;

    public MySqlRunParser(StatementFactory statementFactory) {
        this.statementFactory = statementFactory;
    }

    public Statement init(AbstractDefinition definition) {
        Statement rootStatement = statementFactory.init(definition, RelationSwitch.createRootSwitch());

        return rootStatement;
    }

    @Override
    public Statement parse(AbstractDefinition definition, Object[] args) {
        ParamManager paramManager = new ParamManager(args);

        Statement rootStatement = statementFactory.getOrCreate(definition, paramManager);

        return rootStatement;
    }
}
