package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.mysql.runparser.statement.QueryFromStatement;
import zly.rivulet.mysql.runparser.statement.operate.OperateStatement;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definition.query.main.FromDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

import java.util.List;
import java.util.StringJoiner;

public class FromStatement implements SqlStatement {

    private final FromDefinition definition;

    private final QueryFromStatement mainFrom;

    private final String mainFromAlias;

    private final List<JoinStatement> leftJoins;

    private final List<JoinStatement> rightJoins;

    private final List<JoinStatement> innerJoins;

    public FromStatement(FromDefinition definition, QueryFromStatement mainFrom, String mainFromAlias, List<JoinStatement> leftJoins, List<JoinStatement> rightJoins, List<JoinStatement> innerJoins) {
        this.definition = definition;
        this.mainFrom = mainFrom;
        this.mainFromAlias = mainFromAlias;
        this.leftJoins = leftJoins;
        this.rightJoins = rightJoins;
        this.innerJoins = innerJoins;
    }

    private static class JoinStatement {
        private final QueryFromStatement queryFrom;

        private final String alias;

        private final List<OperateStatement> operateStatementList;

        private JoinStatement(QueryFromStatement queryFrom, String alias, List<OperateStatement> operateStatementList) {
            this.queryFrom = queryFrom;
            this.alias = alias;
            this.operateStatementList = operateStatementList;
        }
    }


    @Override
    public String createStatement() {
        String mainFrom = this.mainFrom.createStatement() + mainFromAlias;
        StringJoiner leftJoiner = new StringJoiner(" ");
        StringJoiner rightJoiner = new StringJoiner(" ");
        StringJoiner innerJoiner = new StringJoiner(" ");
        return
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
            FromDefinition.class,
            (definition, soleFlag, initHelper) -> {
                FromDefinition fromDefinition = (FromDefinition) definition;
                SQLAliasManager aliasManager = initHelper.getAliasManager();
                String mainFromAlias = aliasManager.getAlias(fromDefinition.getMainFromAliasFlag());
                QueryFromMeta from = fromDefinition.getFrom();

                return new FromStatement(fromDefinition, mainFromTableName, mainFromAlias, leftJoins, rightJoins, innerJoins);
            },
            (definition, helper) -> {
                FromDefinition fromDefinition = (FromDefinition) definition;

                return new FromStatement(definition, mainFromTableName, mainFromAlias, leftJoins, rightJoins, innerJoins);
            }
        );
    }
}
