package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.query.main.SelectDefinition;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

import java.util.List;
import java.util.stream.Collectors;

public class SelectStatement implements SqlStatement {

    private final SelectDefinition selectDefinition;

    private final List<MapStatement> mapStatementList;

    private final static String SELECT = "SELECT ";

    public SelectStatement(SelectDefinition selectDefinition, List<MapStatement> mapStatementList) {
        this.selectDefinition = selectDefinition;
        this.mapStatementList = mapStatementList;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(SELECT);
        for (MapStatement mapStatement : collector.createJoiner(Constant.COMMA, mapStatementList)) {
            mapStatement.collectStatement(collector);
        }
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
