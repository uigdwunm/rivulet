package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.mysql.runparser.statement.QueryFromStatement;
import zly.rivulet.mysql.runparser.statement.operate.OperateStatement;
import zly.rivulet.sql.definition.query.join.JoinType;
import zly.rivulet.sql.definition.query.main.JoinRelationDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

public class JoinStatement implements SqlStatement {
    private final QueryFromStatement queryFrom;

    private final String alias;

    private final OperateStatement operateStatement;

    private final JoinType joinType;

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
    public String createStatement() {
        StringBuilder sb = new StringBuilder()
            .append(this.joinType.getPrefix())
            .append(' ')
            .append(this.queryFrom.createStatement());
        String alias = this.alias;
        if (StringUtil.isNotBlank(alias)) {
            sb.append(" AS ").append(alias);
        }
        OperateStatement operateStatement = this.operateStatement;
        if (operateStatement != null) {
            sb.append(" ON ").append(operateStatement.createStatement());
        }
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
