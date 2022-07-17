package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.mysql.runparser.statement.operate.OperateStatement;
import zly.rivulet.sql.definition.query.main.WhereDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

public class WhereStatement implements SqlStatement {

    private final OperateStatement operateStatement;

    public WhereStatement(OperateStatement operateStatement) {
        this.operateStatement = operateStatement;
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
