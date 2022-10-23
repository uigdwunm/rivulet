package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.sql.definition.query.orderby.SortItemDefinition;
import zly.rivulet.sql.describer.query.desc.SortItem;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.List;
import java.util.stream.Collectors;

public class OrderByDefinition extends AbstractDefinition {

    private List<SortItemDefinition> sortItemDefinitionList;

    public OrderByDefinition(SqlParserPortableToolbox toolbox, List<? extends SortItem<?,?>> orderItemList) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        this.sortItemDefinitionList = orderItemList.stream()
            .map(orderItem -> new SortItemDefinition(toolbox, orderItem))
            .collect(Collectors.toList());
    }

    public List<SortItemDefinition> getSortItemDefinitionList() {
        return sortItemDefinitionList;
    }

    public void setSortItemDefinitionList(List<SortItemDefinition> sortItemDefinitionList) {
        this.sortItemDefinitionList = sortItemDefinitionList;
    }

    @Override
    public OrderByDefinition forAnalyze() {
        return null;
    }

}
