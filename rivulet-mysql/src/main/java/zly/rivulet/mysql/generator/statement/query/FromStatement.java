package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.query.main.FromDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

import java.util.List;
import java.util.stream.Collectors;

public class FromStatement implements SqlStatement {

    private final FromDefinition definition;

    private final QueryFromStatement mainFrom;

    private final String mainFromAlias;

    private final List<JoinStatement> joinStatementList;

    private static final String FROM = "FROM ";

    public FromStatement(FromDefinition definition, QueryFromStatement mainFrom, String mainFromAlias, List<JoinStatement> joinStatementList) {
        this.definition = definition;
        this.mainFrom = mainFrom;
        this.mainFromAlias = mainFromAlias;
        this.joinStatementList = joinStatementList;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(FROM);
        this.mainFrom.collectStatement(collector);
        collector.space().append(this.mainFromAlias);
        collector.space();

        if (CollectionUtils.isEmpty(this.joinStatementList)) {
            return;
        }
        for (JoinStatement joinStatement : collector.createJoiner(" ", this.joinStatementList)) {
            joinStatement.collectStatement(collector);
        }
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
