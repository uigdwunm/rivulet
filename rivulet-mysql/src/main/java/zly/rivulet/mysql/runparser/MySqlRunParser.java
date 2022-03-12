package zly.rivulet.mysql.runparser;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.runparser.RuntimeParser;
import zly.rivulet.base.runparser.statement.Statement;
import zly.rivulet.mysql.runparser.statement.MysqlQuerySqlStatement;
import zly.rivulet.sql.runparser.SqlStatementFactory;

public class MysqlRunParser implements RuntimeParser {

    private SqlStatementFactory sqlStatementFactory;

    public MysqlRunParser() {
        SqlStatementFactory sqlStatementFactory = new SqlStatementFactory();
        this.sqlStatementFactory = sqlStatementFactory;
        registerStatement(sqlStatementFactory);
    }

    @Override
    public Fish parse(AbstractDefinition definition, ParamManager paramManager) {
        Statement rootStatement = sqlStatementFactory.getOrCreate(definition, paramManager);
        return null;
    }

    private void registerStatement(SqlStatementFactory sqlStatementFactory) {
        MysqlQuerySqlStatement.registerToFactory(sqlStatementFactory);
    }
}
