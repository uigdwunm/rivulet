package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.sql.parser.toolbox_.SQLParserPortableToolbox;

import java.util.List;
import java.util.stream.Collectors;

public class GroupDefinition extends AbstractDefinition {
    private final List<SingleValueElementDefinition> groupFields;

    private GroupDefinition(CheckCondition checkCondition, List<SingleValueElementDefinition> groupFields) {
        super(CheckCondition.IS_TRUE, null);
        this.groupFields = groupFields;
    }

    public GroupDefinition(SQLParserPortableToolbox toolbox, List<? extends FieldMapping<?,?>> groupFieldList) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        this.groupFields = groupFieldList.stream()
            .map(toolbox::parseSingleValueForCondition)
            .collect(Collectors.toList());
    }

    public List<SingleValueElementDefinition> getGroupFields() {
        return groupFields;
    }

    @Override
    public Copier copier() {
        return new Copier(groupFields);
    }

    public class Copier implements Definition.Copier {

        private List<SingleValueElementDefinition> groupFields;

        public Copier(List<SingleValueElementDefinition> groupFields) {
            this.groupFields = groupFields;
        }

        public void setGroupFields(List<SingleValueElementDefinition> groupFields) {
            this.groupFields = groupFields;
        }

        @Override
        public GroupDefinition copy() {
            return new GroupDefinition(checkCondition, groupFields);
        }
    }
}
