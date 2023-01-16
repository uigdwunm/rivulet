package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.query.main.OrderByDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

import java.util.List;
import java.util.stream.Collectors;

public class OrderByStatement extends SqlStatement {

    private final OrderByDefinition orderByDefinition;

    private final List<SortItemStatement> sortItemStatementList;

    private final static String ORDER_BY = "ORDER BY ";

    public OrderByStatement(OrderByDefinition orderByDefinition, List<SortItemStatement> sortItemStatementList) {
        this.orderByDefinition = orderByDefinition;
        this.sortItemStatementList = sortItemStatementList;
    }

    @Override
    protected int length() {
        return ORDER_BY.length() +
            sortItemStatementList.size() - 1 +
            sortItemStatementList.stream().map(SortItemStatement::getLengthOrCache).reduce(0, Integer::sum);
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(ORDER_BY);
        for (SortItemStatement sortItemStatement : collector.createJoiner(Constant.COMMA, sortItemStatementList)) {
            collector.append(sortItemStatement);
        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            OrderByDefinition.class,
            (definition, soleFlag, initHelper) -> {
                OrderByDefinition orderByDefinition = (OrderByDefinition) definition;
                List<SqlStatement> sortItemStatementList = orderByDefinition.getSortItemDefinitionList().stream()
                    .map(sortItemDefinition -> sqlStatementFactory.warmUp(sortItemDefinition, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new OrderByStatement(orderByDefinition, (List) sortItemStatementList);
            },
            (definition, helper) -> {
                OrderByDefinition orderByDefinition = (OrderByDefinition) definition;
                List<SqlStatement> sortItemStatementList = orderByDefinition.getSortItemDefinitionList().stream()
                    .map(sortItemDefinition -> sqlStatementFactory.getOrCreate(sortItemDefinition, helper))
                    .collect(Collectors.toList());
                return new OrderByStatement(orderByDefinition, (List) sortItemStatementList);
            }
        );
    }
}
