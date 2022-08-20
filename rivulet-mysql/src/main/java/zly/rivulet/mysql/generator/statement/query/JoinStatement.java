package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.operate.OperateStatement;
import zly.rivulet.sql.definition.query.join.JoinType;
import zly.rivulet.sql.definition.query.main.JoinRelationDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class JoinStatement implements SqlStatement {
    private final QueryFromStatement queryFrom;

    private final String alias;

    private final OperateStatement operateStatement;

    private final JoinType joinType;

    private static final String AS = " AS ";

    private static final String ON = " ON ";

    public JoinStatement(QueryFromStatement queryFrom, String alias, OperateStatement operateStatement, JoinType joinType) {
        this.queryFrom = queryFrom;
        this.alias = alias;
        this.operateStatement = operateStatement;
        this.joinType = joinType;
    }

    public QueryFromStatement getQueryFrom() {
        return queryFrom;
    }

    public String getAlias() {
        return alias;
    }

    public OperateStatement getOperateStatement() {
        return operateStatement;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(this.joinType.getPrefix()).space();
        this.queryFrom.singleCollectStatement(collector);
        String alias = this.alias;
        if (StringUtil.isNotBlank(alias)) {
            collector.append(AS).append(alias);
        }
        OperateStatement operateStatement = this.operateStatement;
        if (operateStatement != null) {
            collector.append(ON);
            operateStatement.collectStatement(collector);
        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            JoinRelationDefinition.class,
            (definition, soleFlag, initHelper) -> {
                JoinRelationDefinition joinRelation = (JoinRelationDefinition) definition;
                SQLAliasManager aliasManager = initHelper.getAliasManager();

                QueryFromStatement joinFromStatement = (QueryFromStatement) sqlStatementFactory.init(joinRelation.getJoinModel(), soleFlag.subSwitch(), initHelper);
                String joinFromAlias = aliasManager.getAlias(joinRelation.getAliasFlag());
                OperateStatement operateStatement = (OperateStatement) sqlStatementFactory.init(joinRelation.getOperateContainerDefinition(), soleFlag.subSwitch(), initHelper);
                return new JoinStatement(joinFromStatement, joinFromAlias, operateStatement, joinRelation.getJoinType());
            },
            (definition, helper) -> {
                JoinRelationDefinition joinRelation = (JoinRelationDefinition) definition;
                SQLAliasManager aliasManager = helper.getAliasManager();

                QueryFromStatement joinFromStatement = (QueryFromStatement) sqlStatementFactory.getOrCreate(joinRelation.getJoinModel(), helper);
                String joinFromAlias = aliasManager.getAlias(joinRelation.getAliasFlag());
                OperateStatement operateStatement = (OperateStatement) sqlStatementFactory.getOrCreate(joinRelation.getOperateContainerDefinition(), helper);
                return new JoinStatement(joinFromStatement, joinFromAlias, operateStatement, joinRelation.getJoinType());
            }
        );
    }

}
