package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.describer.join.ComplexDescriber;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.preparser.SQLProxyModelManager;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.SqlPreParser;

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

    public FromDefinition(SqlPreParseHelper sqlPreParseHelper, Class<?> mainFromClass, Object proxyModel) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());

        SqlPreParser sqlPreParser = sqlPreParseHelper.getSqlPreParser();
        SqlDefiner definer = sqlPreParser.getDefiner();
        SQLProxyModelManager sqlProxyModelManager = sqlPreParseHelper.getSQLProxyModelManager();

        if (proxyModel instanceof QueryComplexModel) {
            // 是联表查询对象
            QueryComplexModel queryComplexModel = (QueryComplexModel) proxyModel;
            ComplexDescriber complexDescriber = queryComplexModel.register();
            this.from = sqlProxyModelManager.getQueryMeta(complexDescriber.getModelFrom());
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
            // 单表查询对象
            this.from = definer.createOrGetModelMeta(mainFromClass);
        }
    }

    private JoinRelation convert(SqlPreParseHelper sqlPreParseHelper, ComplexDescriber.Relation<?> desc) {
        SQLProxyModelManager sqlProxyModelManager = sqlPreParseHelper.getSQLProxyModelManager();
        QueryFromMeta queryMeta = sqlProxyModelManager.getQueryMeta(desc.getModelRelation());
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
