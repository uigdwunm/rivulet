package zly.rivulet.mysql.runparser;

import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.runparser.statement.Statement;
import zly.rivulet.sql.runparser.SqlStatementFactory;

public class MySQLFish implements Fish {

    private final Statement statement;
    // sql长度统计
    private int length;

//    // statment工厂
//    private final SqlStatementFactory statementFactory;
//
//    private final ParamManager paramManager;

    protected MySQLFish(Statement statement) {
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
