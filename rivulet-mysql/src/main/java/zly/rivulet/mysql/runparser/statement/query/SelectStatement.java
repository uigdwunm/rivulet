package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.sql.definition.query.main.SelectDefinition;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class SelectStatement implements SqlStatement {

    private final SelectDefinition selectDefinition;

    private final List<MapStatement> mapStatementList;

    public SelectStatement(SelectDefinition selectDefinition, List<MapStatement> mapStatementList) {
        this.selectDefinition = selectDefinition;
        this.mapStatementList = mapStatementList;
    }

    @Override
    public String createStatement() {
        StringJoiner stringJoiner = new StringJoiner(",", "SELECT ", " ");
        for (MapStatement mapStatement : mapStatementList) {
            stringJoiner.add(mapStatement.createStatement());
        }
        return stringJoiner.toString();
    }

    @Override
    public void collectStatement(StringBuilder sqlCollector) {

    }

    @Override
    public void formatGetStatement(FormatCollector formatCollector) {

    }

    public Definition getOriginDefinition() {
        return selectDefinition;
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SelectDefinition.class,
            (definition, soleFlag, initHelper) -> {
                SelectDefinition selectDefinition = (SelectDefinition) definition;
                List<SqlStatement> mapStatementList = selectDefinition.getMapDefinitionList().stream()
                    .map(mapDefinition -> sqlStatementFactory.init(mapDefinition, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new SelectStatement(selectDefinition, (List) mapStatementList);
            },
            (definition, helper) -> {
                SelectDefinition selectDefinition = (SelectDefinition) definition;
                List<SqlStatement> mapStatementList = selectDefinition.getMapDefinitionList().stream()
                    .map(mapDefinition -> sqlStatementFactory.getOrCreate(mapDefinition, helper))
                    .collect(Collectors.toList());
                return new SelectStatement(selectDefinition, (List) mapStatementList);
            }
        );
    }
}
