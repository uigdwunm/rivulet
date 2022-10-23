package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;

import java.util.List;

public class Multiply<F, C> implements Function<F, C> {

    private final List<SingleValueElementDesc<F, C>> itemList;

    public Multiply(List<SingleValueElementDesc<F, C>> itemList) {
        this.itemList = itemList;
    }

    public List<SingleValueElementDesc<F, C>> getItemList() {
        return itemList;
    }
}
