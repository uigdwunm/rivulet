package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;

public class FromDefinition extends AbstractDefinition {
    protected FromDefinition() {
        super(CheckCondition.IS_TRUE);
    }

    public FromDefinition(ModelMeta from) {
        this();
    }

    @Override
    public FromDefinition clone() {
        return null;
    }
}
