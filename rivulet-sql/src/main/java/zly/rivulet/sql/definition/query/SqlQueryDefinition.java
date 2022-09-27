package zly.rivulet.sql.definition.query;

import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.assigner.SQLQueryResultAssigner;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definition.query.main.*;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.query.desc.OrderBy;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;
import zly.rivulet.sql.parser.node.QueryProxyNode;

import java.util.ArrayList;
import java.util.List;

public class SqlQueryDefinition implements SQLBlueprint, QueryFromMeta, SQLSingleValueElementDefinition {

    private final String key;

    private final SqlQueryMetaDesc<?, ?> metaDesc;

    private SelectDefinition selectDefinition;

    private FromDefinition fromDefinition;

    private WhereDefinition whereDefinition;

    private GroupDefinition groupDefinition;

    private HavingDefinition havingDefinition;

    private OrderByDefinition orderByDefinition;

    private SkitDefinition skit;

    private LimitDefinition limit;

    private SQLQueryResultAssigner sqlQueryResultAssigner;

    private final List<AbstractDefinition> subDefinitionList = new ArrayList<>();

    private SQLAliasManager aliasManager;

    private ParamReceiptManager paramReceiptManager;

    private SQLAliasManager.AliasFlag aliasFlag;

    private SqlQueryDefinition(SqlQueryMetaDesc<?, ?> metaDesc) {
        this.key = metaDesc.getKey();
        this.metaDesc = metaDesc;
    }

    public SqlQueryDefinition(SqlParserPortableToolbox toolbox, WholeDesc wholeDesc) {
        SqlQueryMetaDesc<?, ?> metaDesc = (SqlQueryMetaDesc<?, ?>) wholeDesc;
        this.key = metaDesc.getKey();
        QueryProxyNode queryProxyNode = new QueryProxyNode(toolbox, metaDesc.getMainFrom());
        toolbox.setCurrNode(queryProxyNode);

        // 解析赋值
        this.metaDesc = metaDesc;
        this.fromDefinition = new FromDefinition(toolbox);
        this.selectDefinition = new SelectDefinition(
            toolbox,
            this.fromDefinition.getFromMode(),
            metaDesc.getSelectModel(),
            metaDesc.getMappedItemList()
        );

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

        this.sqlQueryResultAssigner = selectDefinition.getSqlAssigner();

        this.aliasManager = SQLAliasManager.create(toolbox.getConfigProperties(), queryProxyNode);
        this.paramReceiptManager = toolbox.getParamDefinitionManager();
        this.aliasFlag = queryProxyNode.getAliasFlag();
    }

    @Override
    public SqlQueryDefinition forAnalyze() {
        SqlQueryDefinition sqlQueryDefinition = new SqlQueryDefinition(this.metaDesc);
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

    public SqlQueryMetaDesc<?, ?> getMetaDesc() {
        return metaDesc;
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
    public String getKey() {
        return this.key;
    }

    @Override
    public RivuletFlag getFlag() {
        return metaDesc.getFlag();
    }

    @Override
    public SQLQueryResultAssigner getAssigner() {
        return this.sqlQueryResultAssigner;
    }

    @Override
    public ParamReceiptManager getParamReceiptManager() {
        return paramReceiptManager;
    }

    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }
}
