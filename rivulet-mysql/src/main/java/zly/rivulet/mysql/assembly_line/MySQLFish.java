package zly.rivulet.mysql.assembly_line;

import zly.rivulet.base.assembly_line.Fish;
import zly.rivulet.base.assembly_line.statement.Statement;

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
