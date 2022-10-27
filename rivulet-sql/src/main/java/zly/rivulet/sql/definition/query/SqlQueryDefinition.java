package zly.rivulet.sql.definition.query;

import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.MapUtils;
import zly.rivulet.sql.assigner.SQLQueryResultAssigner;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.main.*;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.definition.query.operate.AndOperateDefinition;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.definition.query.operate.InOperateDefinition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.custom.SQLPartCustomDesc;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.SortItem;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.proxy_node.FromNode;
import zly.rivulet.sql.parser.proxy_node.ProxyNodeManager;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlQueryDefinition extends SQLBlueprint implements QueryFromMeta, SQLSingleValueElementDefinition {

    private final RivuletFlag flag = RivuletFlag.QUERY;

    private SelectDefinition selectDefinition;

    private FromDefinition fromDefinition;

    private WhereDefinition whereDefinition;

    private GroupDefinition groupDefinition;

    private HavingDefinition havingDefinition;

    private OrderByDefinition orderByDefinition;

    private SkitDefinition skit;

    private LimitDefinition limit;

    private SQLQueryResultAssigner assigner;

    private final List<AbstractDefinition> subDefinitionList = new ArrayList<>();

    private final SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createAlias();

    public SqlQueryDefinition(SqlParserPortableToolbox toolbox, WholeDesc wholeDesc) {
        SqlQueryMetaDesc<?, ?> metaDesc = (SqlQueryMetaDesc<?, ?>) wholeDesc;
        this.paramReceiptManager = toolbox.getParamReceiptManager();

        ProxyNodeManager proxyModelManager = toolbox.getSqlPreParser().getProxyModelManager();
        QueryProxyNode queryProxyNode = proxyModelManager.getOrCreateQueryProxyNode(this, toolbox, metaDesc);
        toolbox.setQueryProxyNode(queryProxyNode);

        this.selectDefinition = new SelectDefinition(toolbox, metaDesc.getSelectModel(), queryProxyNode);
        this.fromDefinition = new FromDefinition(toolbox);

        this.subDefinitionList.add(selectDefinition);
        this.subDefinitionList.add(fromDefinition);
        ConditionContainer<?, ?> whereConditionContainer = metaDesc.getWhereConditionContainer();
        if (whereConditionContainer != null) {
            this.whereDefinition = new WhereDefinition(toolbox, whereConditionContainer);
            this.subDefinitionList.add(this.whereDefinition);
        }

        List<? extends FieldMapping<?, ?>> groupFieldList = metaDesc.getGroupFieldList();
        if (CollectionUtils.isNotEmpty(groupFieldList)) {
            this.groupDefinition = new GroupDefinition(toolbox, groupFieldList);
            this.subDefinitionList.add(this.groupDefinition);
        }
        List<? extends Condition<?, ?>> havingItemList = metaDesc.getHavingItemList();
        if (CollectionUtils.isNotEmpty(havingItemList)) {
            this.havingDefinition = new HavingDefinition(toolbox, havingItemList);
            this.subDefinitionList.add(this.havingDefinition);
        }
        List<? extends SortItem<?, ?>> orderItemList = metaDesc.getOrderItemList();
        if (CollectionUtils.isNotEmpty(orderItemList)) {
            this.orderByDefinition = new OrderByDefinition(toolbox, orderItemList);
            this.subDefinitionList.add(this.orderByDefinition);
        }
        Param<Integer> skit = metaDesc.getSkit();
        if (skit != null) {
            this.skit = new SkitDefinition(toolbox, skit);
            this.subDefinitionList.add(this.skit);
        }
        Param<Integer> limit = metaDesc.getLimit();
        if (limit != null) {
            this.limit = new LimitDefinition(toolbox, limit);
            this.subDefinitionList.add(this.limit);
        }

        Map<Class<? extends Definition>, Param<SQLPartCustomDesc>> customStatementMap = metaDesc.getCustomStatementMap();
        if (MapUtils.isNotEmpty(customStatementMap)) {
            this.customStatementMap = customStatementMap.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> this.paramReceiptManager.registerParam(entry.getValue()))
                );
        }

        this.assigner = queryProxyNode.getAssigner();

        this.aliasManager = new SQLAliasManager(toolbox.getConfigProperties());
        this.aliasManager.init(queryProxyNode);
    }

    public SqlQueryDefinition(SqlParserPortableToolbox toolbox, SQLModelMeta sqlModelMeta, SQLFieldMeta primaryKey) {
        Class<?> modelClass = sqlModelMeta.getModelClass();
        this.aliasManager = new SQLAliasManager(toolbox.getConfigProperties());
        QueryProxyNode queryProxyNode = new QueryProxyNode(this, sqlModelMeta);
        toolbox.setQueryProxyNode(queryProxyNode);
        this.fromDefinition = new FromDefinition(toolbox);
        this.selectDefinition = new SelectDefinition(toolbox, modelClass, queryProxyNode);
        Param<? extends SQLFieldMeta> mainIdParam = Param.of(primaryKey.getClass(), Constant.MAIN_ID, SqlParamCheckType.NATURE);
        Param<? extends SQLFieldMeta> mainIdsParam = Param.of(primaryKey.getClass(), Constant.MAIN_IDS, SqlParamCheckType.NATURE);
        FromNode fromNode = queryProxyNode.getFromNode(0);
        this.whereDefinition = new WhereDefinition(
            toolbox,
            new AndOperateDefinition(
                toolbox,
                new EqOperateDefinition(
                    toolbox,
                    new MapDefinition(primaryKey, fromNode.getAliasFlag(), null),
                    mainIdParam,
                    CheckCondition.notNull(mainIdParam)

                ),
                new InOperateDefinition(
                    toolbox,
                    new MapDefinition(primaryKey, fromNode.getAliasFlag(), null),
                    mainIdsParam,
                    CheckCondition.notEmpty(mainIdsParam)
                )
            )
        );
        this.paramReceiptManager = toolbox.getParamReceiptManager();
        this.customStatementMap = null;
        this.aliasManager.init(queryProxyNode);
    }

    public SqlQueryDefinition() {}

    @Override
    public SqlQueryDefinition forAnalyze() {
        SqlQueryDefinition sqlQueryDefinition = new SqlQueryDefinition();
        sqlQueryDefinition.selectDefinition = selectDefinition.forAnalyze();
        sqlQueryDefinition.fromDefinition = fromDefinition.forAnalyze();
        // TODO 这里要注意
        sqlQueryDefinition.aliasManager = aliasManager;

        if (whereDefinition != null) {
            sqlQueryDefinition.whereDefinition = whereDefinition.forAnalyze();
        }
        if (groupDefinition != null) {
            sqlQueryDefinition.groupDefinition = groupDefinition.forAnalyze();
        }
        if (havingDefinition != null) {
            sqlQueryDefinition.havingDefinition = havingDefinition.forAnalyze();
        }
        if (orderByDefinition != null) {
            sqlQueryDefinition.orderByDefinition = orderByDefinition.forAnalyze();
        }
        if (skit != null) {
            sqlQueryDefinition.skit = skit.forAnalyze();
        }
        if (limit != null) {
            sqlQueryDefinition.limit = limit.forAnalyze();
        }

        return sqlQueryDefinition;
    }

    public SelectDefinition getSelectDefinition() {
        return selectDefinition;
    }

    public FromDefinition getFromDefinition() {
        return fromDefinition;
    }

    public WhereDefinition getWhereDefinition() {
        return whereDefinition;
    }

    public GroupDefinition getGroupDefinition() {
        return groupDefinition;
    }

    public HavingDefinition getHavingDefinition() {
        return havingDefinition;
    }

    public OrderByDefinition getOrderByDefinition() {
        return orderByDefinition;
    }

    public SkitDefinition getSkit() {
        return skit;
    }

    public LimitDefinition getLimit() {
        return limit;
    }

    public List<AbstractDefinition> getSubDefinitionList() {
        return this.subDefinitionList;
    }

    @Override
    public RivuletFlag getFlag() {
        return this.flag;
    }

    @Override
    public SQLQueryResultAssigner getAssigner() {
        return this.assigner;
    }

    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }

}
