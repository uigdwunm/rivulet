package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.query.main.SelectDefinition;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

import java.util.List;
import java.util.stream.Collectors;

public class SelectStatement implements SqlStatement {

    private final SelectDefinition selectDefinition;

    public SelectStatement(SelectDefinition selectDefinition) {
        this.selectDefinition = selectDefinition;
    }

    @Override
    public String createStatement() {
        return null;
    }

    @Override
    public void collectStatement(StringBuilder sqlCollector) {

    }

    @Override
    public void formatGetStatement(FormatCollectHelper formatCollectHelper) {

    }

    @Override
    public Definition getOriginDefinition() {
        return selectDefinition;
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SelectDefinition.class,
            (definition, soleFlag, initHelper) -> {
                SelectDefinition selectDefinition = (SelectDefinition) definition;
                List<SqlStatement> subStatementList = sqlQueryDefinition.getSubDefinitionList().stream()
                    .map(subDefinition -> sqlStatementFactory.init(subDefinition, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new MySqlQueryStatement(selectDefinition);
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
