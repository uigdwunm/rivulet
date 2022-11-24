package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definition.query.join.JoinType;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.describer.join.ComplexDescriber;
import zly.rivulet.sql.describer.condition.JoinConditionContainer;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;
import zly.rivulet.sql.parser.proxy_node.FromNode;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;

public class JoinRelationDefinition extends AbstractDefinition {

    private final QueryFromMeta joinModel;

    private final OperateDefinition operateContainerDefinition;

    private final SQLAliasManager.AliasFlag aliasFlag;

    private final JoinType joinType;

    private JoinRelationDefinition(CheckCondition checkCondition, QueryFromMeta joinModel, OperateDefinition operateContainerDefinition, SQLAliasManager.AliasFlag aliasFlag, JoinType joinType) {
        super(checkCondition, null);
        this.joinModel = joinModel;
            this.operateContainerDefinition = operateContainerDefinition;
            this.aliasFlag = aliasFlag;
            this.joinType = joinType;
        }

    public JoinRelationDefinition(SqlParserPortableToolbox toolbox, ComplexDescriber.Relation<?> desc) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        QueryProxyNode proxyNode = toolbox.getQueryProxyNode();
        JoinConditionContainer<?, ?> conditionContainer = desc.getConditionContainer();
        OperateDefinition operateDefinition = conditionContainer.getOperate().createDefinition(toolbox, conditionContainer);
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
    public Copier copier() {
        return new Copier(joinModel, operateContainerDefinition, aliasFlag ,joinType);
    }

    public class Copier implements Definition.Copier {
        private QueryFromMeta joinModel;

        private OperateDefinition operateContainerDefinition;

        private SQLAliasManager.AliasFlag aliasFlag;

        private JoinType joinType;

        private Copier(QueryFromMeta joinModel, OperateDefinition operateContainerDefinition, SQLAliasManager.AliasFlag aliasFlag, JoinType joinType) {
            this.joinModel = joinModel;
            this.operateContainerDefinition = operateContainerDefinition;
            this.aliasFlag = aliasFlag;
            this.joinType = joinType;
        }

        public void setJoinModel(QueryFromMeta joinModel) {
            this.joinModel = joinModel;
        }

        public void setOperateContainerDefinition(OperateDefinition operateContainerDefinition) {
            this.operateContainerDefinition = operateContainerDefinition;
        }

        public void setJoinType(JoinType joinType) {
            this.joinType = joinType;
        }

        @Override
        public JoinRelationDefinition copy() {
            return new JoinRelationDefinition(checkCondition, joinModel, operateContainerDefinition, aliasFlag, joinType);
        }
    }
}
