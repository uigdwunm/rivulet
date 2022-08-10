package zly.rivulet.mysql.assembly_line;

import zly.rivulet.base.analyzer.Analyzer;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.assembly_line.Fish;
import zly.rivulet.base.assembly_line.AssemblyLine;
import zly.rivulet.base.assembly_line.param_manager.ParamManager;
import zly.rivulet.base.utils.RelationSwitch;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.assembly_line.statement.FieldStatement;
import zly.rivulet.mysql.assembly_line.statement.ModelFromStatement;
import zly.rivulet.mysql.assembly_line.statement.operate.AndOperateStatement;
import zly.rivulet.mysql.assembly_line.statement.operate.EqOperateStatement;
import zly.rivulet.mysql.assembly_line.statement.operate.OrOperateStatement;
import zly.rivulet.mysql.assembly_line.statement.param.SQLParamStatement;
import zly.rivulet.mysql.assembly_line.statement.query.*;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.assembly_line.toolbox.AssemblyLinePortableToolbox;
import zly.rivulet.sql.assembly_line.toolbox.WarmUpToolbox;
import zly.rivulet.sql.assembly_line.SqlStatementFactory;
import zly.rivulet.sql.assembly_line.statement.SqlStatement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MysqlAssemblyLine implements AssemblyLine {

    private final SqlStatementFactory sqlStatementFactory;

    private final MySQLRivuletProperties configProperties;

    private final ConvertorManager convertorManager;

    /**
     * key是设计图，value是流水线过程中用到的工具箱，
     * 如果没查到说明没有初始化过
     **/
    private final Map<Blueprint, Function<ParamManager, AssemblyLinePortableToolbox>> toolboxCreatorMap = new ConcurrentHashMap<>();

    public MysqlAssemblyLine(MySQLRivuletProperties configProperties, ConvertorManager convertorManager) {
        this.sqlStatementFactory = new SqlStatementFactory();
        this.convertorManager = convertorManager;
        this.configProperties = configProperties;
        registerStatement(sqlStatementFactory);
    }

    @Override
    public void warmUp(Blueprint blueprint) {
        if (toolboxCreatorMap.containsKey(blueprint)) {
            // 已经初始化过了
            return;
        }
        SQLBlueprint sqlBlueprint = (SQLBlueprint) blueprint;

        Function<ParamManager, AssemblyLinePortableToolbox> toolboxCreator = this.warmUpAndToolbox(sqlBlueprint);
        toolboxCreatorMap.put(sqlBlueprint, toolboxCreator);
    }

    private Function<ParamManager, AssemblyLinePortableToolbox> warmUpAndToolbox(SQLBlueprint sqlBlueprint) {
        RelationSwitch rootSwitch = RelationSwitch.createRootSwitch();
        SQLAliasManager aliasManager = sqlBlueprint.getAliasManager();
        WarmUpToolbox warmUpToolbox = new WarmUpToolbox(aliasManager);
        sqlStatementFactory.init(sqlBlueprint, rootSwitch, warmUpToolbox);
        return paramManager -> new AssemblyLinePortableToolbox(paramManager, sqlBlueprint.getAliasManager());
    }

    @Override
    public Fish generate(Blueprint blueprint, ParamManager paramManager) {
        SQLBlueprint sqlBlueprint = (SQLBlueprint) blueprint;

        Function<ParamManager, AssemblyLinePortableToolbox> toolboxCreator = toolboxCreatorMap.get(blueprint);
        if (toolboxCreator == null) {
            // 没有初始化过，先初始化
            toolboxCreator = this.warmUpAndToolbox(sqlBlueprint);
            toolboxCreatorMap.put(sqlBlueprint, toolboxCreator);
        }
        // 参数本身不能缓存
        // 有查询条件的父级不能缓存
        // 有排序的容器不能缓存

        AssemblyLinePortableToolbox toolbox = toolboxCreator.apply(paramManager);
        SqlStatement rootStatement = sqlStatementFactory.getOrCreate(sqlBlueprint, toolbox);
        MySQLFish mySQLFish = new MySQLFish(rootStatement);
        return mySQLFish;
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
