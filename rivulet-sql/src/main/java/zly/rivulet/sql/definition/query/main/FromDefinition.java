package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.join.JoinType;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.describer.join.ComplexDescriber;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.FromNode;
import zly.rivulet.sql.preparser.helper.node.ModelProxyNode;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

import java.util.List;
import java.util.stream.Collectors;

public class FromDefinition extends AbstractDefinition {
    private QueryFromMeta from;

    private SQLAliasManager.AliasFlag mainFromAliasFlag;

    private List<JoinRelation> joinRelations;

    public static class JoinRelation {

        private QueryFromMeta joinModel;

        private List<OperateDefinition> operateDefinitionList;

        private SQLAliasManager.AliasFlag aliasFlag;

        private JoinType joinType;

        public JoinRelation(QueryFromMeta joinModel, List<OperateDefinition> operateDefinitionList, SQLAliasManager.AliasFlag aliasFlag, JoinType joinType) {
            this.joinModel = joinModel;
            this.operateDefinitionList = operateDefinitionList;
            this.aliasFlag = aliasFlag;
            this.joinType = joinType;
        }

        public QueryFromMeta getJoinModel() {
            return joinModel;
        }

        public List<OperateDefinition> getOperateDefinitionList() {
            return operateDefinitionList;
        }

        public SQLAliasManager.AliasFlag getAliasFlag() {
            return aliasFlag;
        }

        public JoinType getJoinType() {
            return joinType;
        }
    }

    public FromDefinition(SqlPreParseHelper sqlPreParseHelper) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());

        QueryProxyNode proxyNode = sqlPreParseHelper.getCurrNode();
        Object proxyModel = proxyNode.getProxyModel();

        if (proxyModel instanceof QueryComplexModel) {
            // 是联表查询对象
            QueryComplexModel queryComplexModel = (QueryComplexModel) proxyNode.getProxyModel();
            ComplexDescriber complexDescriber = queryComplexModel.register();

            FromNode mainFromNode = proxyNode.getFromNode(complexDescriber.getModelFrom());
            this.from = mainFromNode.getQueryFromMeta();
            this.mainFromAliasFlag = mainFromNode.getAliasFlag();
            this.joinRelations = complexDescriber.getJoinRelations().stream()
                .map(relation -> convert(sqlPreParseHelper, relation))
                .collect(Collectors.toList());
        } else {
            // 单表查询对象,node里一定有一个唯一的fromNode
            FromNode fromNode = proxyNode.getFromNodeList().get(0);
            ModelProxyNode modelProxyNode = (ModelProxyNode) fromNode;
            this.from = modelProxyNode.getModelMeta();
        }
    }

    private JoinRelation convert(SqlPreParseHelper sqlPreParseHelper, ComplexDescriber.Relation<?> desc) {
        QueryProxyNode proxyNode = sqlPreParseHelper.getCurrNode();
        List<OperateDefinition> operateDefinitionList = desc.getConditionList().stream()
            .map(item -> item.getOperate().createDefinition(sqlPreParseHelper, item))
            .collect(Collectors.toList());
        FromNode fromNode = proxyNode.getFromNode(desc.getModelRelation());
        return new JoinRelation(fromNode.getQueryFromMeta(), operateDefinitionList, fromNode.getAliasFlag(), desc.getJoinType());
    }

    @Override
    public FromDefinition forAnalyze() {
        return null;
    }

    public Class<?> getFromMode() {
        if (from instanceof SQLModelMeta) {
            return ((SQLModelMeta) from).getModelClass();
        } else {
            // 随便返回一个,无意义
            return SqlQueryMetaDesc.class;
        }
    }

    public QueryFromMeta getFrom() {
        return from;
    }

    public SQLAliasManager.AliasFlag getMainFromAliasFlag() {
        return mainFromAliasFlag;
    }

    public List<JoinRelation> getJoinRelations() {
        return joinRelations;
    }
}
