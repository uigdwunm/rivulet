package zly.rivulet.sql.preparser;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.mapper.MapDefinition;
import zly.rivulet.base.preparser.Rock;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.runparser.statement.Statement;
import zly.rivulet.base.utils.RelationSwitch;
import zly.rivulet.sql.runparser.SqlStatementFactory;

public class SqlRock extends Rock {

    private SqlStatementFactory statementFactory;

    protected SqlRock(AbstractDefinition definition, MapDefinition mapDefinition) {
        super(definition, mapDefinition);
        init();
    }

    public void init() {
        SqlStatementFactory statementFactory = new SqlStatementFactory();
        Statement rootStatement = statementFactory.init(super.getDefinition(), RelationSwitch.createRootSwitch());

        this.statementFactory = statementFactory;
    }

    public Statement parse(AbstractDefinition definition, ParamManager paramManager) {
        return statementFactory.getOrCreate(definition, paramManager);
    }
}
