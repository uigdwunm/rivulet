package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.query.main.SelectDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

import java.util.List;
import java.util.stream.Collectors;

public class SelectStatement extends SqlStatement {

    private final SelectDefinition selectDefinition;

    private final List<MapStatement> mapStatementList;

    private final static String SELECT = "SELECT ";

    public SelectStatement(SelectDefinition selectDefinition, List<MapStatement> mapStatementList) {
        this.selectDefinition = selectDefinition;
        this.mapStatementList = mapStatementList;
    }

    @Override
    public int length() {
        return SELECT.length() +
            mapStatementList.size() - 1 +
            mapStatementList.stream().map(MapStatement::selectItemLength).reduce(0, Integer::sum);
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(SELECT);
        for (MapStatement mapStatement : collector.createJoiner(Constant.COMMA, mapStatementList)) {
            mapStatement.selectItemCollectStatement(collector);
        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SelectDefinition.class,
            (definition, soleFlag, initHelper) -> {
                SelectDefinition selectDefinition = (SelectDefinition) definition;
                List<SqlStatement> mapStatementList = selectDefinition.getMapDefinitionList().stream()
                    .map(mapDefinition -> sqlStatementFactory.warmUp(mapDefinition, soleFlag.subSwitch(), initHelper))
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
