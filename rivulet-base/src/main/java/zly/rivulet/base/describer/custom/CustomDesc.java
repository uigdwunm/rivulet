package zly.rivulet.base.describer.custom;

import zly.rivulet.base.describer.SingleValueElementDesc;

import java.util.List;
import java.util.function.BiConsumer;

public interface CustomDesc {

    List<SingleValueElementDesc<?, ?>> getSingleValueList();

    BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect();
}
