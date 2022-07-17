package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.mysql.runparser.statement.QueryFromStatement;
import zly.rivulet.sql.definition.query.main.FromDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class FromStatement implements SqlStatement {

    private final FromDefinition definition;

    private final QueryFromStatement mainFrom;

    private final String mainFromAlias;

    private final List<JoinStatement> joinStatementList;

    public FromStatement(FromDefinition definition, QueryFromStatement mainFrom, String mainFromAlias, List<JoinStatement> joinStatementList) {
        this.definition = definition;
        this.mainFrom = mainFrom;
        this.mainFromAlias = mainFromAlias;
        this.joinStatementList = joinStatementList;
    }

    @Override
    public String createStatement() {
        String mainFrom = "FROM" + this.mainFrom.createStatement() + mainFromAlias;
        StringJoiner joinJoiner = new StringJoiner(" ");
        for (JoinStatement joinStatement : joinStatementList) {
            joinJoiner.add(joinStatement.createStatement());
        }

        return mainFrom + joinJoiner;
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

                QueryFromStatement mainFromStatement = (QueryFromStatement) sqlStatementFactory.init(fromDefinition.getFrom(), soleFlag.subSwitch(), initHelper);
                String mainFromAlias = aliasManager.getAlias(fromDefinition.getMainFromAliasFlag());
                List<JoinStatement> joinStatementList = fromDefinition.getJoinRelations().stream()
                    .map(joinRelation -> (JoinStatement) sqlStatementFactory.init(joinRelation, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new FromStatement(fromDefinition, mainFromStatement, mainFromAlias, joinStatementList);
            },
            (definition, helper) -> {
                FromDefinition fromDefinition = (FromDefinition) definition;
                SQLAliasManager aliasManager = helper.getAliasManager();

                QueryFromStatement mainFromStatement = (QueryFromStatement) sqlStatementFactory.getOrCreate(fromDefinition.getFrom(), helper);
                String mainFromAlias = aliasManager.getAlias(fromDefinition.getMainFromAliasFlag());
                List<JoinStatement> joinStatementList = fromDefinition.getJoinRelations().stream()
                    .map(joinRelation -> (JoinStatement) sqlStatementFactory.getOrCreate(joinRelation, helper))
                    .collect(Collectors.toList());
                return new FromStatement(fromDefinition, mainFromStatement, mainFromAlias, joinStatementList);
            }
        );
    }
}
