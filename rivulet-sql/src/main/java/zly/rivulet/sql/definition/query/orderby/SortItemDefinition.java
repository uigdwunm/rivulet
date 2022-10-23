package zly.rivulet.sql.definition.query.orderby;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.sql.describer.query.desc.SortItem;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class SortItemDefinition {
    private SingleValueElementDefinition singleValue;

    private SortItem.SortType sortType;

    public SortItemDefinition(SqlParserPortableToolbox toolbox, SortItem<?, ?> orderItem) {
        this.singleValue = toolbox.parseSingleValueForCondition(orderItem.getSingleValue());
        this.sortType = orderItem.getSortType();
    }

    public void setSingleValue(SingleValueElementDefinition singleValue) {
        this.singleValue = singleValue;
    }

    public void setSortType(SortItem.SortType sortType) {
        this.sortType = sortType;
    }

    public SingleValueElementDefinition getSingleValue() {
        return singleValue;
    }

    public SortItem.SortType getSortType() {
        return sortType;
    }
}
