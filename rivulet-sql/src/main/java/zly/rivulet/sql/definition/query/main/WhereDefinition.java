package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.discriber.query.desc.Where;

import java.util.List;

public class WhereDefinition extends AbstractDefinition {
    protected WhereDefinition() {
        super(CheckCondition.IS_TRUE);
    }

    public WhereDefinition(List<? extends Where.Item<?,?>> whereItemList) {
        this();
    }

    @Override
    public WhereDefinition clone() {
        return null;
    }
}
