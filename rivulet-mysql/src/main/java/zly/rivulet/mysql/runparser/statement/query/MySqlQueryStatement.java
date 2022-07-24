package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.mysql.runparser.statement.QueryFromStatement;
import zly.rivulet.mysql.runparser.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
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
    public void formatGetStatement(FormatCollector formatCollector) {
        for (SqlStatement statement : subStatementList) {
            statement.formatGetStatement(formatCollector);
        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SqlQueryDefinition.class,
            (definition, soleFlag, initHelper) -> {
                SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) definition;
                List<SqlStatement> subStatementList = sqlQueryDefinition.getSubDefinitionList().stream()
                    .map(subDefinition -> sqlStatementFactory.init(subDefinition, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new MySqlQueryStatement(sqlQueryDefinition, subStatementList);
            },
            (definition, helper) -> {
                SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) definition;
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
