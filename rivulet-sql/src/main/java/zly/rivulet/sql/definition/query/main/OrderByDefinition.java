package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.definition.query.orderby.SortItemDefinition;
import zly.rivulet.sql.describer.query.desc.SortItem;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.List;
import java.util.stream.Collectors;

public class OrderByDefinition extends AbstractDefinition {

    private final List<SortItemDefinition> sortItemDefinitionList;

    public OrderByDefinition(SqlParserPortableToolbox toolbox, List<? extends SortItem<?,?>> orderItemList) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        this.sortItemDefinitionList = orderItemList.stream()
            .map(orderItem -> new SortItemDefinition(toolbox, orderItem))
            .collect(Collectors.toList());
    }

    private OrderByDefinition(CheckCondition checkCondition, List<SortItemDefinition> sortItemDefinitionList) {
        super(checkCondition, null);
        this.sortItemDefinitionList = sortItemDefinitionList;
    }

    public List<SortItemDefinition> getSortItemDefinitionList() {
        return sortItemDefinitionList;
    }

    @Override
    public Copier copier() {
        return new Copier(this.sortItemDefinitionList);
    }

    public class Copier implements Definition.Copier {

        private List<SortItemDefinition> sortItemDefinitionList;

        public Copier(List<SortItemDefinition> sortItemDefinitionList) {
            this.sortItemDefinitionList = sortItemDefinitionList;
        }

        public void setSortItemDefinitionList(List<SortItemDefinition> sortItemDefinitionList) {
            this.sortItemDefinitionList = sortItemDefinitionList;
        }

        @Override
        public OrderByDefinition copy() {
            return new OrderByDefinition(checkCondition, this.sortItemDefinitionList);
        }
    }
}
