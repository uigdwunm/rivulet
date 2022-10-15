package zly.rivulet.sql.definition.query;

import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.sql.assigner.SQLQueryResultAssigner;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.main.*;
import zly.rivulet.sql.definition.query.operate.AndOperateDefinition;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.definition.query.operate.InOperateDefinition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.OrderBy;
import zly.rivulet.sql.generator.statement.SqlStatement;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlQueryDefinition implements SQLBlueprint, QueryFromMeta, SQLSingleValueElementDefinition {

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

    private ParamReceiptManager paramReceiptManager;

    private SQLAliasManager aliasManager;

    private final SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createAlias();

    private boolean isWarmUp = false;

    /**
     * definition类和statement之间的缓存映射
     **/
    private final Map<Definition, SqlStatement> statementCache = new ConcurrentHashMap<>();

    public SqlQueryDefinition(SqlParserPortableToolbox toolbox, WholeDesc wholeDesc) {
        SqlQueryMetaDesc<?, ?> metaDesc = (SqlQueryMetaDesc<?, ?>) wholeDesc;
        this.aliasManager = new SQLAliasManager(toolbox.getConfigProperties());

        QueryProxyNode queryProxyNode = new QueryProxyNode(this, toolbox, metaDesc);
        toolbox.setQueryProxyNode(queryProxyNode);

        this.selectDefinition = new SelectDefinition(toolbox, metaDesc.getSelectModel(), queryProxyNode);
        this.fromDefinition = new FromDefinition(toolbox, metaDesc);

        this.subDefinitionList.add(selectDefinition);
        this.subDefinitionList.add(fromDefinition);
        ConditionContainer<?, ?> whereConditionContainer = metaDesc.getWhereConditionContainer();
        if (whereConditionContainer != null) {
            this.whereDefinition = new WhereDefinition(toolbox, whereConditionContainer);
            this.subDefinitionList.add(this.whereDefinition);
        }

        List<? extends FieldMapping<?, ?>> groupFieldList = metaDesc.getGroupFieldList();
        if (groupFieldList != null) {
            this.groupDefinition = new GroupDefinition(toolbox, groupFieldList);
            this.subDefinitionList.add(this.groupDefinition);
        }
        List<? extends Condition<?, ?>> havingItemList = metaDesc.getHavingItemList();
        if (havingItemList != null) {
            this.havingDefinition = new HavingDefinition(toolbox, havingItemList);
            this.subDefinitionList.add(this.havingDefinition);
        }
        List<? extends OrderBy.Item<?, ?>> orderFieldList = metaDesc.getOrderFieldList();
        if (orderFieldList != null) {
            this.orderByDefinition = new OrderByDefinition(toolbox, orderFieldList);
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

        this.assigner = selectDefinition.getSqlAssigner();

        this.paramReceiptManager = toolbox.getParamReceiptManager();

        this.aliasManager.init(queryProxyNode);
    }

    public SqlQueryDefinition(SqlParserPortableToolbox toolbox, SQLModelMeta sqlModelMeta, SQLFieldMeta primaryKey) {
        Class<?> modelClass = sqlModelMeta.getModelClass();
        this.aliasManager = new SQLAliasManager(toolbox.getConfigProperties());
        new QueryProxyNode(this, toolbox, sqlModelMeta);
        toolbox.setQueryProxyNode(this.queryProxyNode);
        this.fromDefinition = new FromDefinition(toolbox, metaDesc);
        this.selectDefinition = new SelectDefinition(toolbox, modelClass, quer);
        Param<? extends SQLFieldMeta> mainIdParam = Param.of(primaryKey.getClass(), Constant.MAIN_ID, SqlParamCheckType.NATURE);
        Param<? extends SQLFieldMeta> mainIdsParam = Param.of(primaryKey.getClass(), Constant.MAIN_IDS, SqlParamCheckType.NATURE);
        this.whereDefinition = new WhereDefinition(
            toolbox,
            new AndOperateDefinition(
                toolbox,
                new EqOperateDefinition(
                    toolbox,
                    new FieldDefinition(sqlModelMeta, primaryKey),
                    mainIdParam,
                    CheckCondition.notNull(mainIdParam)

                ),
                new InOperateDefinition(
                    toolbox,
                    new FieldDefinition(sqlModelMeta, primaryKey),
                    mainIdsParam,
                    CheckCondition.notEmpty(mainIdsParam)
                )
            )
        );
        this.paramReceiptManager = toolbox.getParamReceiptManager();
    }

    public SqlQueryDefinition(QueryProxyNode queryProxyNode) {
        this.queryProxyNode = queryProxyNode;
    }

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
    public SQLAliasManager getAliasManager() {
        return aliasManager;
    }

    @Override
    public RivuletFlag getFlag() {
        return this.flag;
    }

    @Override
    public SQLQueryResultAssigner getAssigner() {
        return this.assigner;
    }

    @Override
    public ParamReceiptManager getParamReceiptManager() {
        return paramReceiptManager;
    }

    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }

    @Override
    public void putStatement(Definition key, Statement sqlStatement) {
        this.statementCache.put(key, (SqlStatement) sqlStatement);
    }

    @Override
    public SqlStatement getStatement(Definition key) {
        return this.statementCache.get(key);
    }

    @Override
    public boolean isWarmUp() {
        return this.isWarmUp;
    }

    @Override
    public void finishWarmUp() {
        this.isWarmUp = true;
    }

}
