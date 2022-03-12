package zly.rivulet.sql.runparser.statement;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.runparser.statement.Statement;

public abstract class AbstractSqlStatement implements Statement {

    protected final String cache;

    protected AbstractSqlStatement() {
        this.cache = this.createStatement();
    }

    public abstract Definition getOriginDefinition();

}
