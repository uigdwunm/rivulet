package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.discriber.query.desc.Where;

import java.util.List;

public class HavingDefinition extends AbstractDefinition {
    protected HavingDefinition() {
        super(CheckCondition.IS_TRUE);
    }

    public HavingDefinition(List<? extends Where.Item<?,?>> havingItemList) {
        this();
    }

    @Override
    public HavingDefinition clone() {
        return null;
    }
}
