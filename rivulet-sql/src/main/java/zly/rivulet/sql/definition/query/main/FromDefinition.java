package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.describer.join.ComplexDescriber;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.FromNode;
import zly.rivulet.sql.preparser.helper.node.ModelProxyNode;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

import java.util.List;
import java.util.stream.Collectors;

public class FromDefinition extends AbstractDefinition {
    private QueryFromMeta from;

    private List<JoinRelation> leftJoinRelations;

    private List<JoinRelation> rightJoinRelations;

    private List<JoinRelation> innerJoinRelations;

    public static class JoinRelation {

        private QueryFromMeta joinModel;

        private List<OperateDefinition> operateDefinitionList;

        public JoinRelation(QueryFromMeta joinModel, List<OperateDefinition> operateDefinitionList) {
            this.joinModel = joinModel;
            this.operateDefinitionList = operateDefinitionList;
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

            this.from = proxyNode.getFromNode(complexDescriber.getModelFrom()).getQueryFromMeta();
            this.leftJoinRelations = complexDescriber.getLeftJoinRelations().stream()
                .map(relation -> convert(sqlPreParseHelper, relation))
                .collect(Collectors.toList());
            this.rightJoinRelations = complexDescriber.getRightJoinRelations().stream()
                .map(relation -> convert(sqlPreParseHelper, relation))
                .collect(Collectors.toList());
            this.innerJoinRelations = complexDescriber.getInnerJoinRelations().stream()
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
        QueryFromMeta queryMeta = proxyNode.getFromNode(desc.getModelRelation()).getQueryFromMeta();
        List<OperateDefinition> operateDefinitionList = desc.getConditionList().stream()
            .map(item -> item.getOperate().createDefinition(sqlPreParseHelper, item))
            .collect(Collectors.toList());
        return new JoinRelation(queryMeta, operateDefinitionList);
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
}
