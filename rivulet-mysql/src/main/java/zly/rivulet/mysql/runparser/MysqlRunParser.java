package zly.rivulet.mysql.runparser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.runparser.RuntimeParser;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.runparser.statement.query.MySqlQueryStatement;
import zly.rivulet.sql.runparser.SqlRunParseHelper;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

public class MysqlRunParser implements RuntimeParser {

    private final SqlStatementFactory sqlStatementFactory;

    private final MySQLRivuletProperties configProperties;

    private final ConvertorManager convertorManager;

    public MysqlRunParser(MySQLRivuletProperties configProperties, ConvertorManager convertorManager) {
        this.sqlStatementFactory = new SqlStatementFactory();
        this.convertorManager = convertorManager;
        this.configProperties = configProperties;
        registerStatement(sqlStatementFactory);
    }

    @Override
    public Fish parse(FinalDefinition definition, ParamManager paramManager) {
        // 参数本身不能缓存
        // 有查询条件的父级不能缓存
        // 有排序的容器不能缓存
        //
        SqlRunParseHelper sqlRunParseHelper = new SqlRunParseHelper(paramManager);
        SqlStatement rootStatement = sqlStatementFactory.getOrCreate(definition, sqlRunParseHelper);

        return null;
    }

    private void registerStatement(SqlStatementFactory sqlStatementFactory) {
        MySqlQueryStatement.registerToFactory(sqlStatementFactory);
    }
}
