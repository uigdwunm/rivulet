package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomDesc;

import java.util.List;

public abstract class SQLFunction<F, C> implements SingleValueElementDesc<F, C>, CustomDesc {

    private final List<SingleValueElementDesc<F, C>> singleValueElementDescList;

    protected SQLFunction(List<SingleValueElementDesc<F, C>> singleValueElementDescList) {
        this.singleValueElementDescList = singleValueElementDescList;
    }

    @Override
    public List<SingleValueElementDesc<?, ?>> getSingleValueList() {
        return (List) singleValueElementDescList;
    }
}
