package zly.rivulet.mysql.runparser.statement.operate;

import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.sql.definition.query.operate.OrOperateDefinition;
import zly.rivulet.sql.runparser.SqlStatementFactory;

import java.util.List;
import java.util.stream.Collectors;

public class OrOperateStatement implements OperateStatement {

    private final List<OperateStatement> subOperateList;

    public OrOperateStatement(List<OperateStatement> subOperateList) {
        this.subOperateList = subOperateList;
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

    public List<OperateStatement> getSubOperateList() {
        return subOperateList;
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
