package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.QueryFromStatement;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

import java.util.List;
import java.util.stream.Collectors;

public class MySqlQueryStatement implements QueryFromStatement {

    private final SqlQueryDefinition definition;

    /**
     * 子语句列表，select、from、where之类的
     * 有序
     **/
    private final List<SqlStatement> subStatementList;

    private MySqlQueryStatement(SqlQueryDefinition definition, List<SqlStatement> subStatementList) {
        this.definition = definition;
        this.subStatementList = subStatementList;
    }

    public SqlQueryDefinition getOriginDefinition() {
        return this.definition;
    }

    @Override
    public void singleCollectStatement(StatementCollector collector) {
        collector.leftBracket();
        this.collectStatement(collector);
        collector.rightBracket();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        for (SqlStatement subStatement : collector.createJoiner(Constant.SPACE, this.subStatementList)) {
            subStatement.collectStatement(collector);
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
