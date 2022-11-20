package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;

import java.util.List;
import java.util.function.BiConsumer;

public class Multiply<F, C> implements Function<F, C> {

    private final List<SingleValueElementDesc<F, C>> itemList;

    public Multiply(List<SingleValueElementDesc<F, C>> itemList) {
        this.itemList = itemList;
    }

    public List<SingleValueElementDesc<F, C>> getItemList() {
        return itemList;
    }

    @Override
    public List<SingleValueElementDesc<?, ?>> getSingleValueList() {
        return (List) itemList;
    }

    @Override
    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
        return ((customCollector, customSingleValueWraps) -> customCollector.appendAllSeparator(customSingleValueWraps, "*"));
    }
}
