package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.mysql.runparser.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class MySqlQueryStatement implements SingleValueElementStatement {

    private final SqlQueryDefinition definition;

    /**
     * 子语句列表，select、from、where之类的
     * 有序
     **/
    private final List<SqlStatement> subStatementList;

    private final String cache;

    private MySqlQueryStatement(SqlQueryDefinition definition, List<SqlStatement> subStatementList) {
        this.definition = definition;
        this.subStatementList = subStatementList;
        this.cache = this.createStatement();
    }

    @Override
    public SqlQueryDefinition getOriginDefinition() {
        return this.definition;
    }

    @Override
    public String createStatement() {

        StringJoiner stringJoiner = new StringJoiner(" ");
        for (SqlStatement statement : subStatementList) {
            stringJoiner.add(statement.createStatement());
        }

        return stringJoiner.toString();
    }

    @Override
    public void collectStatement(StringBuilder sqlCollector) {
        if (this.cache != null) {
            sqlCollector.append(' ').append(this.cache);
            return;
        }

        for (SqlStatement statement : subStatementList) {
            statement.collectStatement(sqlCollector);
        }
    }

    @Override
    public void formatGetStatement(FormatCollectHelper formatCollectHelper) {
        for (SqlStatement statement : subStatementList) {
            statement.formatGetStatement(formatCollectHelper);
        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SqlQueryDefinition.class,
            (definition, soleFlag, initHelper) -> {
                SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) definition;
                // 解析这个查询类型时必须把别名管理器换成相应的，防止多层子查询别名混乱
                SQLAliasManager outerAliasManager = initHelper.getAliasManager();
                initHelper.setAliasManager(sqlQueryDefinition.getAliasManager());
                List<SqlStatement> subStatementList = sqlQueryDefinition.getSubDefinitionList().stream()
                    .map(subDefinition -> sqlStatementFactory.init(subDefinition, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                // 解析完子查询了，换回来
                initHelper.setAliasManager(outerAliasManager);
                return new MySqlQueryStatement(sqlQueryDefinition, subStatementList);
            },
            (definition, helper) -> {
                SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) definition;
                // 解析这个查询类型时必须把别名管理器换成相应的，防止多层子查询别名混乱
                SQLAliasManager outerAliasManager = helper.getAliasManager();
                helper.setAliasManager(sqlQueryDefinition.getAliasManager());
                ParamManager paramManager = helper.getParamManager();
                List<SqlStatement> subStatementList = sqlQueryDefinition.getSubDefinitionList().stream()
                    .filter(subDefinition -> subDefinition.check(paramManager))
                    .map(subDefinition -> sqlStatementFactory.getOrCreate(subDefinition, helper))
                    .collect(Collectors.toList());

                // 解析完子查询了，换回来
                helper.setAliasManager(outerAliasManager);
                return new MySqlQueryStatement(sqlQueryDefinition, subStatementList);
            }
        );
    }
}
