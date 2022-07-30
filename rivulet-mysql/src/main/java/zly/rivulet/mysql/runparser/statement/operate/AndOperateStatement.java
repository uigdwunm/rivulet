package zly.rivulet.mysql.runparser.statement.operate;

import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.base.utils.StatementCollector;
import zly.rivulet.sql.definition.query.operate.AndOperateDefinition;
import zly.rivulet.sql.runparser.SqlStatementFactory;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class AndOperateStatement implements OperateStatement {

    private final List<OperateStatement> subOperateList;

    private static final String AND_CONNECTOR = " AND ";

    private static final String FORMAT_AND_CONNECTOR = "AND ";

    public AndOperateStatement(List<OperateStatement> subOperateList) {
        this.subOperateList = subOperateList;
    }


    @Override
    public void collectStatement(StatementCollector collector) {
        for (OperateStatement operateStatement : collector.createJoiner(AND_CONNECTOR, subOperateList)) {
            if (operateStatement instanceof AndOperateStatement || operateStatement instanceof OrOperateStatement) {
                collector.leftBracket();
                operateStatement.collectStatement(collector);
                collector.rightBracket();
            } else {
                operateStatement.collectStatement(collector);
            }
        }

    }

    @Override
    public void formatGetStatement(FormatCollector collector) {
        for (OperateStatement operateStatement : collector.createAfterLineConnectorJoiner(FORMAT_AND_CONNECTOR, subOperateList)) {
            if (operateStatement instanceof AndOperateStatement || operateStatement instanceof OrOperateStatement) {
                collector.leftBracketLine();
                collector.tab();
                operateStatement.formatGetStatement(collector);
                collector.returnTab();
                collector.rightBracketLine();
            } else {
                operateStatement.formatGetStatement(collector);
            }
        }
    }

    public List<OperateStatement> getSubOperateList() {
        return subOperateList;
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            AndOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                AndOperateDefinition andOperateDefinition = (AndOperateDefinition) definition;
                List<OperateStatement> operateStatementList = andOperateDefinition.getOperateDefinitionList().stream()
                    .map(subOperation -> (OperateStatement) sqlStatementFactory.init(subOperation, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new AndOperateStatement(operateStatementList);
            },
            (definition, helper) -> {
                AndOperateDefinition andOperateDefinition = (AndOperateDefinition) definition;
                List<OperateStatement> operateStatementList = andOperateDefinition.getOperateDefinitionList().stream()
                    .map(subOperation -> (OperateStatement) sqlStatementFactory.getOrCreate(subOperation, helper))
                    .collect(Collectors.toList());
                return new AndOperateStatement(operateStatementList);
            }
        );
    }
}
