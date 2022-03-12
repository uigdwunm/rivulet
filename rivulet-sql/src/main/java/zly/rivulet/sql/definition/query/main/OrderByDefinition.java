package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.discriber.query.desc.OrderBy;

import java.util.List;

public class OrderByDefinition extends AbstractDefinition {
    protected OrderByDefinition() {
        super(CheckCondition.IS_TRUE);
    }

    public OrderByDefinition(List<? extends OrderBy.Item<?,?>> orderFieldList) {
        this();
    }

    @Override
    public OrderByDefinition clone() {
        return null;
    }
}
