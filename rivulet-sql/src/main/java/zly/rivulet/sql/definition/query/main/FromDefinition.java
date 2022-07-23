package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.describer.join.ComplexDescriber;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.FromNode;
import zly.rivulet.sql.preparser.helper.node.ModelProxyNode;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FromDefinition extends AbstractDefinition {
    private QueryFromMeta from;

    private SQLAliasManager.AliasFlag mainFromAliasFlag;

    private List<JoinRelationDefinition> joinRelations;

    public FromDefinition(SqlPreParseHelper sqlPreParseHelper) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamDefinitionManager());

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
                .map(relation -> new JoinRelationDefinition(sqlPreParseHelper, relation))
                .collect(Collectors.toList());
        } else {
            // 单表查询对象,node里一定有一个唯一的fromNode
            FromNode fromNode = proxyNode.getFromNodeList().get(0);
            ModelProxyNode modelProxyNode = (ModelProxyNode) fromNode;
            this.from = modelProxyNode.getModelMeta();
            this.joinRelations = Collections.emptyList();
        }
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

    public List<JoinRelationDefinition> getJoinRelations() {
        return joinRelations;
    }

    @Override
    public FromDefinition forAnalyze() {
        return null;
    }
}
