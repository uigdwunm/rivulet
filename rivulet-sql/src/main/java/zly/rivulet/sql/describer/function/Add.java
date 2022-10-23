package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;

import java.util.List;

public class Add<F, C> implements Function<F, C> {

    private final List<SingleValueElementDesc<F, C>> addItemList;

    public Add(List<SingleValueElementDesc<F, C>> addItemList) {
        this.addItemList = addItemList;
    }

    public List<SingleValueElementDesc<F, C>> getAddItemList() {
        return addItemList;
    }
}
