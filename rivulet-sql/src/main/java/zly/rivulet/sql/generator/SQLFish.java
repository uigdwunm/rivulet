package zly.rivulet.sql.generator;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class SQLFish implements Fish {

    private final SQLBlueprint blueprint;

    private final SQLStatement statement;
    // sql长度统计
    private final int length;

//    // statment工厂
//    private final SqlStatementFactory statementFactory;
//
//    private final ParamManager paramManager;

    protected SQLFish(SQLBlueprint blueprint, SQLStatement statement) {
        this.blueprint = blueprint;
        this.statement = statement;
        this.length = statement.getLengthOrCache();
    }

    @Override
    public Statement getStatement() {
        return this.statement;
    }

    @Override
    public Blueprint getBlueprint() {
        return blueprint;
    }

    @Override
    public int getLength() {
        return length;
    }
}
