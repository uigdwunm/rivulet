package zly.rivulet.mysql.generator;

import zly.rivulet.base.RivuletProperties;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.parser.Parser;
import zly.rivulet.base.utils.MapUtils;
import zly.rivulet.base.utils.RelationSwitch;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.generator.statement.ModelFromStatement;
import zly.rivulet.mysql.generator.statement.MySQLFieldStatement;
import zly.rivulet.mysql.generator.statement.SQLCustomStatement;
import zly.rivulet.mysql.generator.statement.SQLFunctionStatement;
import zly.rivulet.mysql.generator.statement.delete.MySQLDeleteStatement;
import zly.rivulet.mysql.generator.statement.insert.ColumnItemStatement;
import zly.rivulet.mysql.generator.statement.insert.MySQLInsertStatement;
import zly.rivulet.mysql.generator.statement.operate.*;
import zly.rivulet.mysql.generator.statement.param.SQLParamStatement;
import zly.rivulet.mysql.generator.statement.query.*;
import zly.rivulet.mysql.generator.statement.update.MySQLUpdateStatement;
import zly.rivulet.mysql.generator.statement.update.SetItemStatement;
import zly.rivulet.mysql.generator.statement.update.SetStatement;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.describer.custom.SQLPartCustomDesc;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;
import zly.rivulet.sql.generator.toolbox.SQLGenerateToolbox;
import zly.rivulet.sql.generator.toolbox.WarmUpToolbox;
import zly.rivulet.sql.parser.SqlParser;

import java.util.Map;

public class MysqlGenerator implements Generator {

    private final SqlStatementFactory sqlStatementFactory;

    private final MySQLRivuletProperties configProperties;

    private final ConvertorManager convertorManager;

    private final SqlParser sqlParser;

    public MysqlGenerator(MySQLRivuletProperties configProperties, SqlParser sqlParser) {
        this.sqlStatementFactory = new SqlStatementFactory();
        this.convertorManager = sqlParser.getConvertorManager();
        this.configProperties = configProperties;
        this.sqlParser = sqlParser;
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
        SQLGenerateToolbox toolbox = new SQLGenerateToolbox(paramManager, sqlBlueprint);
        if (paramManager instanceof CommonParamManager) {
            CommonParamManager commonParamManager = (CommonParamManager) paramManager;
            // 仅支持非批量的替换
            // 在这里把custom要替换的Definition放到toolbox中
            Map<Class<? extends Definition>, ParamReceipt> customStatementMap = sqlBlueprint.getCustomStatementMap();
            if (MapUtils.isNotEmpty(customStatementMap)) {
                for (Map.Entry<Class<? extends Definition>, ParamReceipt> entry : customStatementMap.entrySet()) {
                    SQLPartCustomDesc sqlPartCustomDesc = (SQLPartCustomDesc) commonParamManager.getParam(entry.getValue());
                    toolbox.putReplaceDefinition(entry.getKey(), sqlParser.parseCustom(sqlBlueprint.getRivuletKey(), sqlPartCustomDesc));
                }
            }
        }
        SqlStatement rootStatement = sqlStatementFactory.getOrCreate(sqlBlueprint, toolbox);
        return new MySQLFish(sqlBlueprint, rootStatement);
    }

    @Override
    public Parser getParser() {
        return this.sqlParser;
    }

    @Override
    public RivuletProperties getProperties() {
        return this.configProperties;
    }

    @Override
    public ConvertorManager getConvertorManager() {
        return this.convertorManager;
    }

    private void registerStatement(SqlStatementFactory sqlStatementFactory) {
        SQLParamStatement.registerToFactory(sqlStatementFactory);

        SQLCustomStatement.registerToFactory(sqlStatementFactory);
        SQLFunctionStatement.registerToFactory(sqlStatementFactory);

        MySqlQueryStatement.registerToFactory(sqlStatementFactory);
        SelectStatement.registerToFactory(sqlStatementFactory);
        ModelFromStatement.registerToFactory(sqlStatementFactory);
        MySQLFieldStatement.registerToFactory(sqlStatementFactory);
        MapStatement.registerToFactory(sqlStatementFactory);

        FromStatement.registerToFactory(sqlStatementFactory);
        JoinStatement.registerToFactory(sqlStatementFactory);

        WhereStatement.registerToFactory(sqlStatementFactory);
        BetweenOperateStatement.registerToFactory(sqlStatementFactory);
        EqOperateStatement.registerToFactory(sqlStatementFactory);
        InOperateStatement.registerToFactory(sqlStatementFactory);
        AndOperateStatement.registerToFactory(sqlStatementFactory);
        OrOperateStatement.registerToFactory(sqlStatementFactory);

        OrderByStatement.registerToFactory(sqlStatementFactory);
        SortItemStatement.registerToFactory(sqlStatementFactory);

        LimitStatement.registerToFactory(sqlStatementFactory);

        MySQLInsertStatement.registerToFactory(sqlStatementFactory);

        ColumnItemStatement.registerToFactory(sqlStatementFactory);

        MySQLDeleteStatement.registerToFactory(sqlStatementFactory);

        MySQLUpdateStatement.registerToFactory(sqlStatementFactory);
        SetItemStatement.registerToFactory(sqlStatementFactory);
        SetStatement.registerToFactory(sqlStatementFactory);
    }
}
