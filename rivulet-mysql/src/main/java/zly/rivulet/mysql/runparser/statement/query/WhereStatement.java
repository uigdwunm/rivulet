package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.base.utils.StatementCollector;
import zly.rivulet.mysql.runparser.statement.operate.OperateStatement;
import zly.rivulet.sql.definition.query.main.WhereDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

public class WhereStatement implements SqlStatement {

    private final OperateStatement operateStatement;

    private static final String WHERE = "WHERE ";

    public WhereStatement(OperateStatement operateStatement) {
        this.operateStatement = operateStatement;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(WHERE);
        operateStatement.collectStatement(collector);
    }

    @Override
    public void formatGetStatement(FormatCollector formatCollector) {
        formatCollector.append(WHERE).line();
        formatCollector.tab();
        this.operateStatement.formatGetStatement(formatCollector);
        formatCollector.returnTab();
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            WhereDefinition.class,
            (definition, soleFlag, initHelper) -> {
                WhereDefinition whereDefinition = (WhereDefinition) definition;
                SQLAliasManager aliasManager = initHelper.getAliasManager();

                OperateStatement operateStatement = (OperateStatement) sqlStatementFactory.init(whereDefinition.getOperateDefinition(), soleFlag.subSwitch(), initHelper);
                return new WhereStatement(operateStatement);
            },
            (definition, helper) -> {
                WhereDefinition whereDefinition = (WhereDefinition) definition;
                SQLAliasManager aliasManager = helper.getAliasManager();

                OperateStatement operateStatement = (OperateStatement) sqlStatementFactory.getOrCreate(whereDefinition.getOperateDefinition(), helper);
                return new WhereStatement(operateStatement);
            }
        );
    }
}
