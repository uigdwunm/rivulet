package zly.rivulet.mysql.runparser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.assembly_line.Fish;
import zly.rivulet.base.assembly_line.AssemblyLine;
import zly.rivulet.base.assembly_line.param_manager.ParamManager;
import zly.rivulet.base.utils.RelationSwitch;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.runparser.statement.FieldStatement;
import zly.rivulet.mysql.runparser.statement.ModelFromStatement;
import zly.rivulet.mysql.runparser.statement.operate.AndOperateStatement;
import zly.rivulet.mysql.runparser.statement.operate.EqOperateStatement;
import zly.rivulet.mysql.runparser.statement.operate.OrOperateStatement;
import zly.rivulet.mysql.runparser.statement.param.SQLParamStatement;
import zly.rivulet.mysql.runparser.statement.query.*;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.runparser.SqlRunParseHelper;
import zly.rivulet.sql.runparser.SqlRunParseInitHelper;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MysqlAssemblyLine implements AssemblyLine {

    private final SqlStatementFactory sqlStatementFactory;

    private final MySQLRivuletProperties configProperties;

    private final ConvertorManager convertorManager;

    /**
     * 记录是否初始化过，
     * k和v都是finalDefinition，只是没有相应的并发Set，懒得整了
     **/
    private final Map<Blueprint, Object> isInitRecord = new ConcurrentHashMap<>();

    public MysqlAssemblyLine(MySQLRivuletProperties configProperties, ConvertorManager convertorManager) {
        this.sqlStatementFactory = new SqlStatementFactory();
        this.convertorManager = convertorManager;
        this.configProperties = configProperties;
        registerStatement(sqlStatementFactory);
    }

    @Override
    public void warmUp(Blueprint blueprint) {
        SQLBlueprint sqlBlueprint = (SQLBlueprint) blueprint;
        Object o = isInitRecord.get(sqlBlueprint);
        if (o != null) {
            // 已经初始化过了
            return;
        }
        RelationSwitch rootSwitch = RelationSwitch.createRootSwitch();
        SQLAliasManager aliasManager = sqlBlueprint.getAliasManager();
        SqlRunParseInitHelper sqlRunParseInitHelper = new SqlRunParseInitHelper(aliasManager);
        sqlStatementFactory.init(sqlBlueprint, rootSwitch, sqlRunParseInitHelper);
        isInitRecord.put(sqlBlueprint, blueprint);
    }

    @Override
    public Fish generate(Blueprint definition, ParamManager paramManager) {
        // 参数本身不能缓存
        // 有查询条件的父级不能缓存
        // 有排序的容器不能缓存
        //
        SQLBlueprint sqlFinalDefinition = (SQLBlueprint) definition;
        // 先初始化
        warmUp(sqlFinalDefinition);

        SqlRunParseHelper sqlRunParseHelper = new SqlRunParseHelper(paramManager, sqlFinalDefinition.getAliasManager());
        SqlStatement rootStatement = sqlStatementFactory.getOrCreate(sqlFinalDefinition, sqlRunParseHelper);

        return new MySQLFish(rootStatement);
    }

    private void registerStatement(SqlStatementFactory sqlStatementFactory) {
        SQLParamStatement.registerToFactory(sqlStatementFactory);

        MySqlQueryStatement.registerToFactory(sqlStatementFactory);
        SelectStatement.registerToFactory(sqlStatementFactory);
        ModelFromStatement.registerToFactory(sqlStatementFactory);
        MapStatement.registerToFactory(sqlStatementFactory);
        FieldStatement.registerToFactory(sqlStatementFactory);

        FromStatement.registerToFactory(sqlStatementFactory);
        JoinStatement.registerToFactory(sqlStatementFactory);

        WhereStatement.registerToFactory(sqlStatementFactory);
        EqOperateStatement.registerToFactory(sqlStatementFactory);
        AndOperateStatement.registerToFactory(sqlStatementFactory);
        OrOperateStatement.registerToFactory(sqlStatementFactory);
    }
}
