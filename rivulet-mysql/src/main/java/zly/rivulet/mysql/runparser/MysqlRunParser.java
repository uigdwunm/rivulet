package zly.rivulet.mysql.runparser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.runparser.RuntimeParser;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.utils.RelationSwitch;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.runparser.statement.FieldStatement;
import zly.rivulet.mysql.runparser.statement.query.MapStatement;
import zly.rivulet.mysql.runparser.statement.query.MySqlQueryStatement;
import zly.rivulet.mysql.runparser.statement.query.SelectStatement;
import zly.rivulet.sql.runparser.SqlRunParseHelper;
import zly.rivulet.sql.runparser.SqlRunParseInitHelper;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class MysqlRunParser implements RuntimeParser {

    private final SqlStatementFactory sqlStatementFactory;

    private final MySQLRivuletProperties configProperties;

    private final ConvertorManager convertorManager;

    /**
     * 记录是否初始化过，
     * k和v都是finalDefinition，只是没有相应的并发Set，懒得整了
     **/
    private final Map<FinalDefinition, Object> isInitRecord = new ConcurrentHashMap<>();

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

        // 先初始化
        initStatement(definition);

        SqlRunParseHelper sqlRunParseHelper = new SqlRunParseHelper(paramManager);
        SqlStatement rootStatement = sqlStatementFactory.getOrCreate(definition, sqlRunParseHelper);

        return null;
    }

    public void initStatement(FinalDefinition definition) {
        Object o = isInitRecord.get(definition);
        if (o != null) {
            // 已经初始化过了
            return;
        }
        RelationSwitch rootSwitch = RelationSwitch.createRootSwitch();
        SqlRunParseInitHelper sqlRunParseInitHelper = new SqlRunParseInitHelper();
        sqlStatementFactory.init(definition, rootSwitch, sqlRunParseInitHelper);
        isInitRecord.put(definition, definition);
    }

    private void registerStatement(SqlStatementFactory sqlStatementFactory) {
        MySqlQueryStatement.registerToFactory(sqlStatementFactory);
        SelectStatement.registerToFactory(sqlStatementFactory);
        MapStatement.registerToFactory(sqlStatementFactory);
        FieldStatement.registerToFactory(sqlStatementFactory);
    }
}
