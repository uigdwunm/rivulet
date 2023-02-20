package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.query.operate.OrOperateDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;

import java.util.List;
import java.util.stream.Collectors;

public class OrOperateStatement extends OperateStatement {

    private final List<OperateStatement> subOperateList;

    public static final String OR_CONNECTOR = "OR ";

    public OrOperateStatement(List<OperateStatement> subOperateList) {
        this.subOperateList = subOperateList;
    }

    @Override
    public int length() {
        int length = 0;
        length += subOperateList.size() * OR_CONNECTOR.length() - 1;
        for (OperateStatement operateStatement : subOperateList) {
            if (operateStatement instanceof AndOperateStatement || operateStatement instanceof OrOperateStatement) {
                length += 1;
                length += operateStatement.getLengthOrCache();
                length += 1;
            } else {
                length += operateStatement.getLengthOrCache();
            }
            length += 1;
        }
        return length;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        for (OperateStatement operateStatement : collector.createJoiner(OR_CONNECTOR, subOperateList)) {
            if (operateStatement instanceof AndOperateStatement || operateStatement instanceof OrOperateStatement) {
                collector.leftBracket();
                collector.append(operateStatement);
                collector.rightBracket();
            } else {
                collector.append(operateStatement);
            }
            collector.space();
        }
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            OrOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                OrOperateDefinition orOperateDefinition = (OrOperateDefinition) definition;
                List<OperateStatement> operateStatementList = orOperateDefinition.getOperateDefinitionList().stream()
                    .map(subOperation -> (OperateStatement) sqlStatementFactory.warmUp(subOperation, soleFlag.subSwitch(), initHelper))
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
