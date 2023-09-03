package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomDesc;

import java.util.List;

public abstract class SQLFunction<C> implements SingleValueElementDesc<C>, CustomDesc {

    private final List<SingleValueElementDesc<C>> singleValueElementDescList;

    private final Class<C> targetType;

    protected SQLFunction(List<SingleValueElementDesc<C>> singleValueElementDescList, Class<C> targetType) {
        this.singleValueElementDescList = singleValueElementDescList;
        this.targetType = targetType;
    }

    @Override
    public Class<C> getTargetType() {
        return this.targetType;
    }

    @Override
    public List<SingleValueElementDesc<?>> getSingleValueList() {
        return (List) singleValueElementDescList;
    }
}
