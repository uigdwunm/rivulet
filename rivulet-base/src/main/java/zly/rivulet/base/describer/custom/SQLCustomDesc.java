package zly.rivulet.base.describer.custom;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.utils.collector.StatementCollector;

import java.util.List;
import java.util.function.BiConsumer;

public interface SQLCustomDesc {

    List<SingleValueElementDesc<?, ?>> getSingleValueList();

    BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect();
}
