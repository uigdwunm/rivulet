package zly.rivulet.sql.definition.query;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.mapper.MapDefinition;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definition.query.main.*;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Condition;
import zly.rivulet.sql.describer.query.desc.OrderBy;
import zly.rivulet.sql.preparser.SqlParamDefinitionManager;
import zly.rivulet.sql.preparser.SqlPreParseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlQueryDefinition implements FinalDefinition, QueryFromMeta {

    private final SqlQueryMetaDesc<?, ?> metaDesc;

    private SelectDefinition selectDefinition;

    private FromDefinition fromDefinition;

    private WhereDefinition whereDefinition;

    private GroupDefinition groupDefinition;

    private HavingDefinition havingDefinition;

    private OrderByDefinition orderByDefinition;

    private SkitDefinition skit;

    private LimitDefinition limit;

    private MapDefinition mapDefinition;

    private final List<AbstractDefinition> subDefinitionList = new ArrayList<>();

    private SqlQueryDefinition(SqlQueryMetaDesc<?, ?> metaDesc) {
        this.metaDesc = metaDesc;
    }

    public SqlQueryDefinition(SqlPreParseHelper sqlPreParseHelper,  WholeDesc wholeDesc) {
        SqlQueryMetaDesc<?, ?> metaDesc = (SqlQueryMetaDesc<?, ?>) wholeDesc;

        // 标记进入了一层子查询(用于解析别名)
        sqlPreParseHelper.startSub();

        // 解析赋值
        this.metaDesc = metaDesc;
        this.fromDefinition = FromDefinition.createFromDefinition(sqlPreParseHelper, metaDesc.getModelFrom(), metaDesc.getSubQueryFrom());
        this.selectDefinition = new SelectDefinition(
            sqlPreParseHelper,
            this.fromDefinition,
            metaDesc.getSelectModel(),
            metaDesc.getMappedItemList(),
            metaDesc.isNameMapped()
        );

        this.subDefinitionList.add(selectDefinition);
        this.subDefinitionList.add(fromDefinition);
        List<? extends Condition<?, ?, ?>> whereItemList = metaDesc.getWhereItemList();
        if (whereItemList != null) {
            this.whereDefinition = new WhereDefinition(sqlPreParseHelper, whereItemList);
            this.subDefinitionList.add(this.whereDefinition);
        }

        List<? extends FieldMapping<?, ?>> groupFieldList = metaDesc.getGroupFieldList();
        if (groupFieldList != null) {
            this.groupDefinition = new GroupDefinition(sqlPreParseHelper, groupFieldList);
            this.subDefinitionList.add(this.groupDefinition);
        }
        List<? extends Condition<?, ?, ?>> havingItemList = metaDesc.getHavingItemList();
        if (havingItemList != null) {
            this.havingDefinition = new HavingDefinition(sqlPreParseHelper, havingItemList);
            this.subDefinitionList.add(this.havingDefinition);
        }
        List<? extends OrderBy.Item<?, ?>> orderFieldList = metaDesc.getOrderFieldList();
        if (orderFieldList != null) {
            this.orderByDefinition = new OrderByDefinition(sqlPreParseHelper, orderFieldList);
            this.subDefinitionList.add(this.orderByDefinition);
        }
        Param<Integer> skit = metaDesc.getSkit();
        if (skit != null) {
            this.skit = new SkitDefinition(sqlPreParseHelper, skit);
            this.subDefinitionList.add(this.skit);
        }
        Param<Integer> limit = metaDesc.getLimit();
        if (limit != null) {
            this.limit = new LimitDefinition(sqlPreParseHelper, limit);
            this.subDefinitionList.add(this.limit);
        }

        this.mapDefinition = selectDefinition.getMapDefinition();

        // 标记进入了一层子查询(用于解析别名)
        sqlPreParseHelper.endSub();
    }

    @Override
    public SqlQueryDefinition forAnalyze() {
        SqlQueryDefinition sqlQueryDefinition = new SqlQueryDefinition(this.metaDesc);
        sqlQueryDefinition.selectDefinition = selectDefinition.forAnalyze();
        sqlQueryDefinition.fromDefinition = fromDefinition.forAnalyze();

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

    @Override
    public MapDefinition getMapDefinition() {
        return mapDefinition;
    }

    public List<AbstractDefinition> getSubDefinitionList() {
        return this.subDefinitionList;
    }
}
