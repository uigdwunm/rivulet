package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.query.main.FromDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;
import zly.rivulet.sql.parser.SQLAliasManager;

import java.util.List;
import java.util.stream.Collectors;

public class FromStatement extends SQLStatement {

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
    public int length() {
        int length = 0;
        length += FROM.length();
        length += this.mainFrom.singleLength();
        if (StringUtil.isNotBlank(this.mainFromAlias)) {
            length += 1 + this.mainFromAlias.length();
        }
        length += 1;
        if (CollectionUtils.isNotEmpty(this.joinStatementList)) {
            length += joinStatementList.size() - 1;
            length += joinStatementList.stream().map(JoinStatement::getLengthOrCache).reduce(0, Integer::sum);
        }
        return length;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(FROM);
        mainFrom.singleCollectStatement(collector);
        if (StringUtil.isNotBlank(this.mainFromAlias)) {
            collector.space().append(this.mainFromAlias);
        }
        collector.space();

        if (CollectionUtils.isEmpty(this.joinStatementList)) {
            return;
        }
        for (JoinStatement joinStatement : collector.createJoiner(" ", this.joinStatementList)) {
            joinStatement.collectStatement(collector);
        }
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            FromDefinition.class,
            (definition, soleFlag, initHelper) -> {
                FromDefinition fromDefinition = (FromDefinition) definition;
                SQLAliasManager aliasManager = initHelper.getAliasManager();

                QueryFromStatement mainFromStatement = (QueryFromStatement) sqlStatementFactory.warmUp(fromDefinition.getMainFrom(), soleFlag.subSwitch(), initHelper);
                String mainFromAlias = aliasManager.getAlias(fromDefinition.getMainFromAliasFlag());
                List<JoinStatement> joinStatementList = fromDefinition.getJoinRelations().stream()
                    .map(joinRelation -> (JoinStatement) sqlStatementFactory.warmUp(joinRelation, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new FromStatement(fromDefinition, mainFromStatement, mainFromAlias, joinStatementList);
            },
            (definition, helper) -> {
                FromDefinition fromDefinition = (FromDefinition) definition;
                SQLAliasManager aliasManager = helper.getAliasManager();

                QueryFromStatement mainFromStatement = (QueryFromStatement) sqlStatementFactory.getOrCreate(fromDefinition.getMainFrom(), helper);
                String mainFromAlias = aliasManager.getAlias(fromDefinition.getMainFromAliasFlag());
                List<JoinStatement> joinStatementList = fromDefinition.getJoinRelations().stream()
                    .map(joinRelation -> (JoinStatement) sqlStatementFactory.getOrCreate(joinRelation, helper))
                    .collect(Collectors.toList());
                return new FromStatement(fromDefinition, mainFromStatement, mainFromAlias, joinStatementList);
            }
        );
    }
}
