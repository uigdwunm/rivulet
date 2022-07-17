package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definer.meta.JoinQueryMeta;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definition.query.join.JoinType;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.describer.join.ComplexDescriber;
import zly.rivulet.sql.describer.query.condition.JoinConditionContainer;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.FromNode;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

public class JoinRelationDefinition extends AbstractDefinition {

    private QueryFromMeta joinModel;

    private OperateDefinition operateContainerDefinition;

    private SQLAliasManager.AliasFlag aliasFlag;

    private JoinType joinType;

    public JoinRelationDefinition(SqlPreParseHelper sqlPreParseHelper, ComplexDescriber.Relation<?> desc) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());
        QueryProxyNode proxyNode = sqlPreParseHelper.getCurrNode();
        JoinConditionContainer<?, ?> conditionContainer = desc.getConditionContainer();
        OperateDefinition operateDefinition = conditionContainer.getOperate().createDefinition(sqlPreParseHelper, conditionContainer);
        FromNode fromNode = proxyNode.getFromNode(desc.getModelRelation());

        this.joinModel = fromNode.getQueryFromMeta();
        this.operateContainerDefinition = operateDefinition;
        this.aliasFlag = fromNode.getAliasFlag();
        this.joinType = desc.getJoinType();
    }

    public QueryFromMeta getJoinModel() {
        return joinModel;
    }

    public OperateDefinition getOperateContainerDefinition() {
        return operateContainerDefinition;
    }

    public SQLAliasManager.AliasFlag getAliasFlag() {
        return aliasFlag;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
