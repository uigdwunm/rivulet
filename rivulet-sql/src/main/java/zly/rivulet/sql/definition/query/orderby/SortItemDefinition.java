package zly.rivulet.sql.definition.query.orderby;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.sql.describer.query.desc.SortItem;
import zly.rivulet.sql.parser.toolbox.SQLParserPortableToolbox;

public class SortItemDefinition extends AbstractDefinition {
    private SingleValueElementDefinition singleValue;

    private SortItem.SortType sortType;

    public SortItemDefinition(CheckCondition checkCondition, SingleValueElementDefinition singleValue, SortItem.SortType sortType) {
        super(checkCondition, null);
        this.singleValue = singleValue;
        this.sortType = sortType;
    }

    public SortItemDefinition(SQLParserPortableToolbox toolbox, SortItem<?, ?> orderItem) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
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

    @Override
    public Copier copier() {
        return new Copier(this.singleValue, sortType);
    }

    public class Copier implements Definition.Copier {

        private SingleValueElementDefinition singleValue;

        private SortItem.SortType sortType;

        private Copier(SingleValueElementDefinition singleValue, SortItem.SortType sortType) {
            this.singleValue = singleValue;
            this.sortType = sortType;
        }

        public void setSingleValue(SingleValueElementDefinition singleValue) {
            this.singleValue = singleValue;
        }

        public void setSortType(SortItem.SortType sortType) {
            this.sortType = sortType;
        }

        @Override
        public SortItemDefinition copy() {
            return new SortItemDefinition(checkCondition, this.singleValue, sortType);
        }
    }
}
