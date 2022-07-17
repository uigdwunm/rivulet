package zly.rivulet.mysql.runparser.statement.operate;

import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.sql.definition.query.operate.AndOperateDefinition;
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
            OrOperateStatement.class,
            (definition, soleFlag, initHelper) -> {
                OrOperateStatement orOperateDefinition = (OrOperateStatement) definition;
                List<OperateStatement> operateStatementList = orOperateDefinition.getSubOperateList().stream()
                    .map(subOperation -> (OperateStatement) sqlStatementFactory.init(subOperation, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new OrOperateStatement(operateStatementList);
            },
            (definition, helper) -> {
                OrOperateStatement orOperateDefinition = (OrOperateStatement) definition;
                List<OperateStatement> operateStatementList = orOperateDefinition.getSubOperateList().stream()
                    .map(subOperation -> (OperateStatement) sqlStatementFactory.getOrCreate(subOperation, helper))
                    .collect(Collectors.toList());
                return new OrOperateStatement(operateStatementList);
            }
        );
    }
}
