package zly.rivulet.mysql.generator.statement.operate;

import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.query.operate.AndOperateDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;

import java.util.List;
import java.util.stream.Collectors;

public class AndOperateStatement extends OperateStatement {

    private final List<OperateStatement> subOperateList;

    public static final String AND_CONNECTOR = " AND ";

    public AndOperateStatement(List<OperateStatement> subOperateList) {
        this.subOperateList = subOperateList;
    }


    @Override
    protected int length() {
        int length = 0;
        length += (subOperateList.size() - 1) * AND_CONNECTOR.length();
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

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            AndOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                AndOperateDefinition andOperateDefinition = (AndOperateDefinition) definition;
                List<OperateStatement> operateStatementList = andOperateDefinition.getOperateDefinitionList().stream()
                    .map(subOperation -> (OperateStatement) sqlStatementFactory.warmUp(subOperation, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new AndOperateStatement(operateStatementList);
            },
            (definition, helper) -> {
                AndOperateDefinition andOperateDefinition = (AndOperateDefinition) definition;
                CommonParamManager paramManager = helper.getParamManager();
                List<OperateStatement> operateStatementList = andOperateDefinition.getOperateDefinitionList().stream()
                    .filter(subOperation -> subOperation.check(paramManager))
                    .map(subOperation -> (OperateStatement) sqlStatementFactory.getOrCreate(subOperation, helper))
                    .collect(Collectors.toList());
                return new AndOperateStatement(operateStatementList);
            }
        );
    }
}
