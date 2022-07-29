package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.base.utils.StatementCollector;
import zly.rivulet.sql.definition.query.main.SelectDefinition;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

import java.util.List;
import java.util.stream.Collectors;

public class SelectStatement implements SqlStatement {

    private final SelectDefinition selectDefinition;

    private final List<MapStatement> mapStatementList;

    private final static String SELECT = "SELECT ";

    private final static String COMMA = ",";

    public SelectStatement(SelectDefinition selectDefinition, List<MapStatement> mapStatementList) {
        this.selectDefinition = selectDefinition;
        this.mapStatementList = mapStatementList;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(SELECT);
        for (MapStatement mapStatement : collector.createJoiner(COMMA, mapStatementList)) {
            mapStatement.collectStatement(collector);
        }
    }

    @Override
    public void formatGetStatement(FormatCollector collector) {
        collector.append(SELECT);
        collector.line();
        collector.tab();
        for (MapStatement mapStatement : collector.createLineJoiner(COMMA, mapStatementList)) {
            mapStatement.formatGetStatement(collector);
        }
        collector.returnTab();
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
