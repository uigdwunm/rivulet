package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definition.query.join.JoinType;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.select.item.JoinItem;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.toolbox.SQLParserPortableToolbox;

public class JoinRelationDefinition extends AbstractDefinition {

    private final QueryFromMeta joinModel;

    private final OperateDefinition operateContainerDefinition;

    private final JoinType joinType;

    private JoinRelationDefinition(CheckCondition checkCondition, QueryFromMeta joinModel, OperateDefinition operateContainerDefinition, JoinType joinType) {
        super(checkCondition, null);
        this.joinModel = joinModel;
        this.operateContainerDefinition = operateContainerDefinition;
        this.joinType = joinType;
    }

    public JoinRelationDefinition(SQLParserPortableToolbox toolbox, JoinItem joinItem) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        this.joinModel = toolbox.parseQueryFromMeta(joinItem.getJoinTable());
        this.joinType = joinItem.getJoinType();
        this.operateContainerDefinition = toolbox.parseCondition(joinItem.getOnConditionContainer());
    }

    public QueryFromMeta getJoinModel() {
        return joinModel;
    }

    public OperateDefinition getOperateContainerDefinition() {
        return operateContainerDefinition;
    }

    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.joinModel.getAliasFlag();
    }

    public JoinType getJoinType() {
        return joinType;
    }

    @Override
    public Copier copier() {
        return new Copier(joinModel, operateContainerDefinition, joinType);
    }

    public class Copier implements Definition.Copier {
        private QueryFromMeta joinModel;

        private OperateDefinition operateContainerDefinition;

        private JoinType joinType;

        private Copier(QueryFromMeta joinModel, OperateDefinition operateContainerDefinition, JoinType joinType) {
            this.joinModel = joinModel;
            this.operateContainerDefinition = operateContainerDefinition;
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
            return new JoinRelationDefinition(checkCondition, joinModel, operateContainerDefinition, joinType);
        }
    }
}
