package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.mysql.runparser.statement.QueryFromStatement;
import zly.rivulet.mysql.runparser.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class MySqlQueryStatement implements SingleValueElementStatement, QueryFromStatement {

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
                // 别名管理器
                SQLAliasManager aliasManager = initHelper.getAliasManager();
                if (aliasManager == null) {
                    // 如果没有，说明是第一次进来，就从查询中塞进去
                    aliasManager = sqlQueryDefinition.getAliasManager();
                    initHelper.setAliasManager(aliasManager);
                }
                List<SqlStatement> subStatementList = sqlQueryDefinition.getSubDefinitionList().stream()
                    .map(subDefinition -> sqlStatementFactory.init(subDefinition, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new MySqlQueryStatement(sqlQueryDefinition, subStatementList);
            },
            (definition, helper) -> {
                SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) definition;
                // 别名管理器
                SQLAliasManager aliasManager = helper.getAliasManager();
                if (aliasManager == null) {
                    // 如果没有，说明是第一次进来，就从查询中塞进去
                    aliasManager = sqlQueryDefinition.getAliasManager();
                    helper.setAliasManager(aliasManager);
                }
                ParamManager paramManager = helper.getParamManager();
                List<SqlStatement> subStatementList = sqlQueryDefinition.getSubDefinitionList().stream()
                    .filter(subDefinition -> subDefinition.check(paramManager))
                    .map(subDefinition -> sqlStatementFactory.getOrCreate(subDefinition, helper))
                    .collect(Collectors.toList());

                return new MySqlQueryStatement(sqlQueryDefinition, subStatementList);
            }
        );
    }
}
