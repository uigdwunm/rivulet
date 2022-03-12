package zly.rivulet.mysql.runparser.statement;

import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.AbstractSqlStatement;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class MysqlQuerySqlStatement extends AbstractSqlStatement {

    private final SqlQueryDefinition definition;

    /**
     * 子语句列表，select、from、where之类的
     **/
    private final List<AbstractSqlStatement> subStatementList;

    private final String cache;

    private MysqlQuerySqlStatement(SqlQueryDefinition definition, List<AbstractSqlStatement> subStatementList) {
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
        for (AbstractSqlStatement statement : subStatementList) {
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

        for (AbstractSqlStatement statement : subStatementList) {
            statement.collectStatement(sqlCollector);
        }
    }

    @Override
    public String formatGetStatement(int tabLevel) {
        String tab = StringUtil.multiStr("  ", tabLevel);
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator() + tab);
        for (AbstractSqlStatement statement : subStatementList) {
            stringJoiner.add(statement.formatGetStatement(tabLevel + 1));
        }
        return stringJoiner.toString();
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SqlQueryDefinition.class,
            (definition, soleFlag, statementFactory) -> {
                SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) definition;
                List<AbstractSqlStatement> subStatementList = sqlQueryDefinition.getSubDefinitionList().stream()
                    .map(subDefinition -> sqlStatementFactory.init(subDefinition, soleFlag.subSwitch()))
                    .collect(Collectors.toList());
                return new MysqlQuerySqlStatement(sqlQueryDefinition, subStatementList);
            },
            (definition, paramManager, statementFactory) -> {
                SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) definition;
                List<AbstractSqlStatement> subStatementList = sqlQueryDefinition.getSubDefinitionList().stream()
                    .map(subDefinition -> sqlStatementFactory.getOrCreate(subDefinition, paramManager))
                    .collect(Collectors.toList());
                return new MysqlQuerySqlStatement(sqlQueryDefinition, subStatementList);
            }
        );
    }
}
