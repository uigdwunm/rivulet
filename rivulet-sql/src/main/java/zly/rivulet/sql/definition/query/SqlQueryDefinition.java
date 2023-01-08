package zly.rivulet.sql.definition.query;

import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.MapUtils;
import zly.rivulet.sql.assigner.SQLQueryResultAssigner;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.definition.query.main.*;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.definition.query.operate.AndOperateDefinition;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.definition.query.operate.InOperateDefinition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.custom.SQLPartCustomDesc;
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
        super(RivuletFlag.QUERY, wholeDesc);
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
        ConditionContainer<?, ?> havingConditionContainer = metaDesc.getHavingConditionContainer();
        if (havingConditionContainer != null) {
            this.havingDefinition = new HavingDefinition(toolbox, havingConditionContainer);
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
        super(RivuletFlag.QUERY, null);
        Class<?> modelClass = sqlModelMeta.getModelClass();
        this.aliasManager = new SQLAliasManager(toolbox.getConfigProperties());
        // 生成queryProxyNode
        ProxyNodeManager proxyModelManager = toolbox.getSqlPreParser().getProxyModelManager();
        QueryProxyNode queryProxyNode = proxyModelManager.getOrCreateQueryProxyNode(toolbox, sqlModelMeta);
        toolbox.setQueryProxyNode(queryProxyNode);
        this.fromDefinition = new FromDefinition(toolbox);
        this.selectDefinition = new SelectDefinition(toolbox, modelClass, queryProxyNode);
        Param<?> mainIdParam = Param.of(ClassUtils.toBoxType(primaryKey.getFieldType()), Constant.MAIN_ID, ParamCheckType.NATURE);
        Param<?> mainIdsParam = Param.of(ClassUtils.toBoxType(primaryKey.getFieldType()), Constant.MAIN_IDS, ParamCheckType.NATURE);
        FromNode fromNode = queryProxyNode.getFromNodeList().get(0);
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

        this.subDefinitionList.add(selectDefinition);
        this.subDefinitionList.add(fromDefinition);
        this.subDefinitionList.add(whereDefinition);

        this.paramReceiptManager = toolbox.getParamReceiptManager();
        this.customStatementMap = null;
        this.aliasManager.init(queryProxyNode);
        this.assigner = queryProxyNode.getAssigner();
    }

    public SqlQueryDefinition(
        WholeDesc wholeDesc,
        SelectDefinition selectDefinition,
        FromDefinition fromDefinition,
        WhereDefinition whereDefinition,
        GroupDefinition groupDefinition,
        HavingDefinition havingDefinition,
        OrderByDefinition orderByDefinition,
        SkitDefinition skit,
        LimitDefinition limit,
        SQLQueryResultAssigner assigner,
        ParamReceiptManager paramReceiptManager,
        SQLAliasManager aliasManager
    ) {
        super(RivuletFlag.QUERY, wholeDesc);
        this.selectDefinition = selectDefinition;
        this.subDefinitionList.add(selectDefinition);
        this.fromDefinition = fromDefinition;
        this.subDefinitionList.add(fromDefinition);
        if (whereDefinition != null) {
            this.whereDefinition = whereDefinition;
            this.subDefinitionList.add(whereDefinition);
        }
        if (groupDefinition != null) {
            this.groupDefinition = groupDefinition;
            this.subDefinitionList.add(groupDefinition);
        }
        if (havingDefinition != null) {
            this.havingDefinition = havingDefinition;
            this.subDefinitionList.add(havingDefinition);
        }
        if (orderByDefinition != null) {
            this.orderByDefinition = orderByDefinition;
            this.subDefinitionList.add(orderByDefinition);
        }
        if (skit != null) {
            this.skit = skit;
            this.subDefinitionList.add(skit);
        }
        if (limit != null) {
            this.limit = limit;
            this.subDefinitionList.add(limit);
        }
        this.assigner = assigner;
        this.paramReceiptManager = paramReceiptManager;
        this.aliasManager = aliasManager;
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
        return RivuletFlag.QUERY;
    }

    @Override
    public SQLQueryResultAssigner getAssigner() {
        return this.assigner;
    }

    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }

    @Override
    public Copier copier() {
        return new Copier();
    }

    public class Copier implements Definition.Copier {

        private SelectDefinition.Copier selectDefinitionCopier;

        private FromDefinition.Copier fromDefinitionCopier;

        private WhereDefinition.Copier whereDefinitionCopier;

        private GroupDefinition.Copier groupDefinitionCopier;

        private HavingDefinition.Copier havingDefinitionCopier;

        private OrderByDefinition.Copier orderByDefinitionCopier;

        private SkitDefinition.Copier skitCopier;

        private LimitDefinition.Copier limitCopier;

        private SQLAliasManager.Copier aliasManagerCopier;

        public void setSelectDefinitionCopier(SelectDefinition.Copier selectDefinitionCopier) {
            this.selectDefinitionCopier = selectDefinitionCopier;
        }

        public void setFromDefinitionCopier(FromDefinition.Copier fromDefinitionCopier) {
            this.fromDefinitionCopier = fromDefinitionCopier;
        }

        public void setWhereDefinitionCopier(WhereDefinition.Copier whereDefinitionCopier) {
            this.whereDefinitionCopier = whereDefinitionCopier;
        }

        public void setGroupDefinitionCopier(GroupDefinition.Copier groupDefinitionCopier) {
            this.groupDefinitionCopier = groupDefinitionCopier;
        }

        public void setHavingDefinitionCopier(HavingDefinition.Copier havingDefinitionCopier) {
            this.havingDefinitionCopier = havingDefinitionCopier;
        }

        public void setOrderByDefinitionCopier(OrderByDefinition.Copier orderByDefinitionCopier) {
            this.orderByDefinitionCopier = orderByDefinitionCopier;
        }

        public void setSkitCopier(SkitDefinition.Copier skitCopier) {
            this.skitCopier = skitCopier;
        }

        public void setLimitCopier(LimitDefinition.Copier limitCopier) {
            this.limitCopier = limitCopier;
        }

        public void setAliasManagerCopier(SQLAliasManager.Copier aliasManagerCopier) {
            this.aliasManagerCopier = aliasManagerCopier;
        }

        @Override
        public SqlQueryDefinition copy() {
            return new SqlQueryDefinition(
                wholeDesc,
                selectDefinitionCopier != null ? selectDefinitionCopier.copy() : selectDefinition,
                fromDefinitionCopier != null ? fromDefinitionCopier.copy() : fromDefinition,
                whereDefinitionCopier != null ? whereDefinitionCopier.copy() : whereDefinition,
                groupDefinitionCopier != null ? groupDefinitionCopier.copy() : groupDefinition,
                havingDefinitionCopier != null ? havingDefinitionCopier.copy() : havingDefinition,
                orderByDefinitionCopier != null ? orderByDefinitionCopier.copy() : orderByDefinition,
                skitCopier != null ? skitCopier.copy() : skit,
                limitCopier != null ? limitCopier.copy() : limit,
                assigner,
                paramReceiptManager,
                aliasManagerCopier != null ? aliasManagerCopier.copy() : aliasManager
            );
        }
    }
}
