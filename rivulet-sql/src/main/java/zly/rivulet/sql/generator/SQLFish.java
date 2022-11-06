package zly.rivulet.sql.generator;

import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class SQLFish implements Fish {

    private final SQLBlueprint blueprint;

    private final SqlStatement statement;
    // sql长度统计
    private int length;

//    // statment工厂
//    private final SqlStatementFactory statementFactory;
//
//    private final ParamManager paramManager;

    protected SQLFish(SQLBlueprint blueprint, SqlStatement statement) {
        this.blueprint = blueprint;
        this.statement = statement;
    }

    @Override
    public Statement getStatement() {
        return this.statement;
    }

    public int getLength() {
        return length;
    }
}
