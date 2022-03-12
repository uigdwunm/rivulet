package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.field.FieldMapping;

import java.util.List;

public class GroupDefinition extends AbstractDefinition {
    protected GroupDefinition() {
        super(CheckCondition.IS_TRUE);
    }

    public GroupDefinition(List<? extends FieldMapping<?,?>> groupFieldList) {
        this();
    }

    @Override
    public GroupDefinition clone() {
        return null;
    }
}
