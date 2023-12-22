package zly.rivulet.base.definer.meta;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;

public abstract class ColumnMeta<C> implements SingleValueElementDesc<C> , SingleValueElementDefinition {

    protected final String name;


    protected final Class<C> type;

    protected ColumnMeta(String name, Class<C> type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public Class<C> getTargetType() {
        return this.type;
    }

    public String getName() {
        return name;
    }


    @Override
    public Copier copier() {
        return new ThisCopier<>(this);
    }

}
