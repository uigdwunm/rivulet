package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.describer.join.ComplexDescriber;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.proxy_node.FromNode;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FromDefinition extends AbstractDefinition {
    private QueryFromMeta mainFrom;

    private SQLAliasManager.AliasFlag mainFromAliasFlag;

    private List<JoinRelationDefinition> joinRelations;

    public FromDefinition(SqlParserPortableToolbox toolbox, SqlQueryMetaDesc<?, ?> metaDesc) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());

        QueryProxyNode proxyNode = toolbox.getQueryProxyNode();
        Object proxyModel = proxyNode.getProxyModel();

        if (proxyModel instanceof QueryComplexModel) {
            // 是复杂查询对象
            QueryComplexModel queryComplexModel = (QueryComplexModel) proxyNode.getProxyModel();
            ComplexDescriber complexDescriber = queryComplexModel.register();

            FromNode mainFromNode = proxyNode.getFromNode(complexDescriber.getModelFrom());
            this.mainFrom = mainFromNode.getQueryFromMeta();
            this.mainFromAliasFlag = mainFromNode.getAliasFlag();
            this.joinRelations = complexDescriber.getJoinRelations().stream()
                .map(relation -> new JoinRelationDefinition(sqlPreParseHelper, relation))
                .collect(Collectors.toList());
        } else {
            // 单表查询对象,node里一定有一个唯一的fromNode
            FromNode fromNode = proxyNode.getFromNodeList().get(0);
            ModelProxyNode modelProxyNode = (ModelProxyNode) fromNode;
            this.mainFrom = modelProxyNode.getModelMeta();
            this.mainFromAliasFlag = fromNode.getAliasFlag();
            this.joinRelations = Collections.emptyList();
        }
    }

    public Class<?> getFromMode() {
        if (mainFrom instanceof SQLModelMeta) {
            return ((SQLModelMeta) mainFrom).getModelClass();
        } else {
            // 随便返回一个,无意义
            return SqlQueryMetaDesc.class;
        }
    }

    public QueryFromMeta getMainFrom() {
        return mainFrom;
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
