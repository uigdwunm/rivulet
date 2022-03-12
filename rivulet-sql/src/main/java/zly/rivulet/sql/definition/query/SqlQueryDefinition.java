package zly.rivulet.sql.definition.query;

import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.mapper.MapDefinition;
import pers.zly.sql.definition.query.main.*;
import zly.rivulet.sql.definition.query.main.*;
import zly.rivulet.sql.discriber.query.SqlQueryMetaDesc;
import zly.rivulet.sql.preparser.SqlParamDefinitionManager;

public class SqlQueryDefinition implements FinalDefinition {

    private final SqlQueryMetaDesc<?, ?> metaDesc;

    private SelectDefinition selectDefinition;

    private FromDefinition fromDefinition;

    private WhereDefinition whereDefinition;

    private GroupDefinition groupDefinition;

    private HavingDefinition havingDefinition;

    private OrderByDefinition orderByDefinition;

    private ParamDefinition skit;

    private ParamDefinition limit;

    private MapDefinition mapDefinition;

    public SqlQueryDefinition(SqlQueryMetaDesc<?, ?> metaDesc, SqlParamDefinitionManager sqlParamDefinitionManager) {
        this.metaDesc = metaDesc;
        this.selectDefinition = new SelectDefinition(metaDesc.getSelectModel(), metaDesc.getMappedItemList(), metaDesc.isNameMapped());
        this.fromDefinition = new FromDefinition(metaDesc.getFrom());
        this.whereDefinition = new WhereDefinition(metaDesc.getWhereItemList());
        this.groupDefinition = new GroupDefinition(metaDesc.getGroupFieldList());
        this.havingDefinition = new HavingDefinition(metaDesc.getHavingItemList());
        this.orderByDefinition = new OrderByDefinition(metaDesc.getOrderFieldList());
        this.skit = sqlParamDefinitionManager.register(metaDesc.getSkit());
        this.limit = sqlParamDefinitionManager.register(metaDesc.getLimit());
        this.mapDefinition =
    }

    public SelectDefinition getSelectDefinition() {
        return selectDefinition;
    }

    public void setSelectDefinition(SelectDefinition selectDefinition) {
        this.selectDefinition = selectDefinition;
    }

    public FromDefinition getFromDefinition() {
        return fromDefinition;
    }

    public void setFromDefinition(FromDefinition fromDefinition) {
        this.fromDefinition = fromDefinition;
    }

    public WhereDefinition getWhereDeifinition() {
        return whereDefinition;
    }

    public void setWhereDeifinition(WhereDefinition whereDefinition) {
        this.whereDefinition = whereDefinition;
    }

    public GroupDefinition getGroupDefinition() {
        return groupDefinition;
    }

    public void setGroupDefinition(GroupDefinition groupDefinition) {
        this.groupDefinition = groupDefinition;
    }

    public HavingDefinition getHavingDefinition() {
        return havingDefinition;
    }

    public void setHavingDefinition(HavingDefinition havingDefinition) {
        this.havingDefinition = havingDefinition;
    }

    public OrderByDefinition getOrderByDefinition() {
        return orderByDefinition;
    }

    public void setOrderByDefinition(OrderByDefinition orderByDefinition) {
        this.orderByDefinition = orderByDefinition;
    }

    public ParamDefinition getSkit() {
        return skit;
    }

    public void setSkit(ParamDefinition skit) {
        this.skit = skit;
    }

    public ParamDefinition getLimit() {
        return limit;
    }

    public void setLimit(ParamDefinition limit) {
        this.limit = limit;
    }

    @Override
    public SqlQueryDefinition clone() {
        SqlQueryDefinition sqlQueryDefinition = new SqlQueryDefinition();
        sqlQueryDefinition.selectDefinition = selectDefinition.clone();
        sqlQueryDefinition.fromDefinition = fromDefinition.clone();

        if (whereDefinition != null) {
            sqlQueryDefinition.whereDefinition = whereDefinition.clone();
        }
        if (groupDefinition != null) {
            sqlQueryDefinition.groupDefinition = groupDefinition.clone();
        }
        if (havingDefinition != null) {
            sqlQueryDefinition.havingDefinition = havingDefinition.clone();
        }
        if (orderByDefinition != null) {
            sqlQueryDefinition.orderByDefinition = orderByDefinition.clone();
        }
        if (skit != null) {
            sqlQueryDefinition.skit = skit;
        }
        if (limit != null) {
            sqlQueryDefinition.limit = limit;
        }
        return sqlQueryDefinition;
    }
}
