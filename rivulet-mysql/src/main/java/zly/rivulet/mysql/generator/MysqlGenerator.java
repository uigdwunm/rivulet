package zly.rivulet.mysql.generator;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.utils.RelationSwitch;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.generator.statement.FieldStatement;
import zly.rivulet.mysql.generator.statement.ModelFromStatement;
import zly.rivulet.mysql.generator.statement.operate.AndOperateStatement;
import zly.rivulet.mysql.generator.statement.operate.EqOperateStatement;
import zly.rivulet.mysql.generator.statement.operate.OrOperateStatement;
import zly.rivulet.mysql.generator.statement.param.SQLParamStatement;
import zly.rivulet.mysql.generator.statement.query.*;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.generator.toolbox.GenerateToolbox;
import zly.rivulet.sql.generator.toolbox.WarmUpToolbox;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class MysqlGenerator implements Generator {

    private final SqlStatementFactory sqlStatementFactory;

    private final MySQLRivuletProperties configProperties;

    private final ConvertorManager convertorManager;

    public MysqlGenerator(MySQLRivuletProperties configProperties, ConvertorManager convertorManager) {
        this.sqlStatementFactory = new SqlStatementFactory();
        this.convertorManager = convertorManager;
        this.configProperties = configProperties;
        registerStatement(sqlStatementFactory);
    }

    @Override
    public void warmUp(Blueprint blueprint) {
        if (blueprint.isWarmUp()) {
            // 已经初始化过了
            return;
        }
        SQLBlueprint sqlBlueprint = (SQLBlueprint) blueprint;

        RelationSwitch rootSwitch = RelationSwitch.createRootSwitch();
        WarmUpToolbox warmUpToolbox = new WarmUpToolbox(sqlBlueprint);
        sqlStatementFactory.warmUp(sqlBlueprint, rootSwitch, warmUpToolbox);
        blueprint.finishWarmUp();
    }


    @Override
    public Fish generate(Blueprint blueprint, ParamManager paramManager) {
        SQLBlueprint sqlBlueprint = (SQLBlueprint) blueprint;
        if (!sqlBlueprint.isWarmUp()) {
            // 没有初始化过，先初始化
            this.warmUp(sqlBlueprint);
        }
        // 参数本身不能缓存
        // 有查询条件的父级不能缓存
        // 有排序的容器不能缓存

        GenerateToolbox toolbox = new GenerateToolbox(paramManager, sqlBlueprint);
        // TODO 在这里把custom要替换的Definition放到toolbox中
        SqlStatement rootStatement = sqlStatementFactory.getOrCreate(sqlBlueprint, toolbox);
        return new MySQLFish(sqlBlueprint, rootStatement);
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
