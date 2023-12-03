package zly.rivulet.sql.definition.query.mapping;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;

public class SelectItemDefinition implements Definition {

    private final SingleValueElementDefinition valueDefinition;

    /**
     * 父引用，没有则为空
     **/
    private final Object reference;

    public SelectItemDefinition(SingleValueElementDefinition valueDefinition, Object reference) {
        this.valueDefinition = valueDefinition;
        this.reference = reference;
    }

    public SingleValueElementDefinition getValueDefinition() {
        return valueDefinition;
    }

    public Object getReference() {
        return reference;
    }

    @Override
    public Copier copier() {
        return new Copier(valueDefinition, reference);
    }

    public static class Copier implements Definition.Copier {

        private SingleValueElementDefinition valueDefinition;

        private Object reference;

        public Copier(SingleValueElementDefinition valueDefinition, Object reference) {
            this.valueDefinition = valueDefinition;
            this.reference = reference;
        }

        public void setValueDefinition(SingleValueElementDefinition valueDefinition) {
            this.valueDefinition = valueDefinition;
        }

        public void setReference(Object reference) {
            this.reference = reference;
        }

        @Override
        public SelectItemDefinition copy() {
            return new SelectItemDefinition(valueDefinition, reference);
        }
    }
}
