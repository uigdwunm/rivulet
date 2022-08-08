package zly.rivulet.mysql.assembly_line.statement.operate;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.query.operate.OrOperateDefinition;
import zly.rivulet.sql.assembly_line.SqlStatementFactory;

import java.util.List;
import java.util.stream.Collectors;

public class OrOperateStatement implements OperateStatement {

    private final List<OperateStatement> subOperateList;

    public static final String OR_CONNECTOR = "OR ";

    public OrOperateStatement(List<OperateStatement> subOperateList) {
        this.subOperateList = subOperateList;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        for (OperateStatement operateStatement : collector.createJoiner(OR_CONNECTOR, subOperateList)) {
            if (operateStatement instanceof AndOperateStatement || operateStatement instanceof OrOperateStatement) {
                collector.leftBracket();
                operateStatement.collectStatement(collector);
                collector.rightBracket();
            } else {
                operateStatement.collectStatement(collector);
            }
            collector.space();
        }

    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            OrOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                OrOperateDefinition orOperateDefinition = (OrOperateDefinition) definition;
                List<OperateStatement> operateStatementList = orOperateDefinition.getOperateDefinitionList().stream()
                    .map(subOperation -> (OperateStatement) sqlStatementFactory.init(subOperation, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new OrOperateStatement(operateStatementList);
            },
            (definition, helper) -> {
                OrOperateDefinition orOperateDefinition = (OrOperateDefinition) definition;
                List<OperateStatement> operateStatementList = orOperateDefinition.getOperateDefinitionList().stream()
                    .map(subOperation -> (OperateStatement) sqlStatementFactory.getOrCreate(subOperation, helper))
                    .collect(Collectors.toList());
                return new OrOperateStatement(operateStatementList);
            }
        );
    }
}
