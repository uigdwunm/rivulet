package zly.rivulet.mysql.runparser.statement.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.mysql.runparser.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

public class EqOperateStatement implements OperateStatement {

    private final SingleValueElementStatement leftValue;

    private final SingleValueElementStatement rightValue;

    public EqOperateStatement(SingleValueElementStatement leftValue, SingleValueElementStatement rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
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

    public Definition getOriginDefinition() {
        return null;
    }


    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            EqOperateDefinition.class,
            (definition, soleFlag, initHelper) -> {
                EqOperateDefinition eqOperateDefinition = (EqOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.init(eqOperateDefinition.getLeftElement(), soleFlag.subSwitch(), initHelper);
                SqlStatement rightStatement = sqlStatementFactory.init(eqOperateDefinition.getRightElement(), soleFlag.subSwitch(), initHelper);
                return new EqOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            },
            (definition, helper) -> {
                EqOperateDefinition eqOperateDefinition = (EqOperateDefinition) definition;
                SqlStatement leftStatement = sqlStatementFactory.getOrCreate(eqOperateDefinition.getLeftElement(), helper);
                SqlStatement rightStatement = sqlStatementFactory.getOrCreate(eqOperateDefinition.getRightElement(), helper);
                return new EqOperateStatement((SingleValueElementStatement) leftStatement, (SingleValueElementStatement) rightStatement);
            }
        );
    }
}
