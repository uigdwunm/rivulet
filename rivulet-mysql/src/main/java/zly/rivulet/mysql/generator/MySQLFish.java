package zly.rivulet.mysql.generator;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.statement.Statement;

public class MySQLFish implements Fish {

    private final Blueprint blueprint;

    private final Statement statement;
    // sql长度统计
    private int length;

//    // statment工厂
//    private final SqlStatementFactory statementFactory;
//
//    private final ParamManager paramManager;

    protected MySQLFish(Blueprint blueprint, Statement statement) {
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
