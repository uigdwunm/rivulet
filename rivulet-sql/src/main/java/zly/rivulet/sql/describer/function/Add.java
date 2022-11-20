package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;

import java.util.List;
import java.util.function.BiConsumer;

public class Add<F, C> implements Function<F, C> {

    private final List<SingleValueElementDesc<F, C>> addItemList;

    public Add(List<SingleValueElementDesc<F, C>> addItemList) {
        this.addItemList = addItemList;
    }

    public List<SingleValueElementDesc<F, C>> getAddItemList() {
        return addItemList;
    }

    @Override
    public List<SingleValueElementDesc<?, ?>> getSingleValueList() {
        return (List) addItemList;
    }

    @Override
    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
        return ((customCollector, customSingleValueWraps) -> customCollector.appendAllSeparator(customSingleValueWraps, "+"));
    }
}
