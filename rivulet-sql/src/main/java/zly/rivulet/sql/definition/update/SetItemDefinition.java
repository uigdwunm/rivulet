package zly.rivulet.sql.definition.update;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.sql.definition.field.FieldDefinition;

public class SetItemDefinition {

    private final SingleValueElementDefinition valueDefinition;

    private final FieldDefinition fieldDefinition;

    public SetItemDefinition(SingleValueElementDefinition valueDefinition, FieldDefinition fieldDefinition) {
        this.valueDefinition = valueDefinition;
        this.fieldDefinition = fieldDefinition;
    }
}
