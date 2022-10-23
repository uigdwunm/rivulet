package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class SubTract<F, C> implements Function<F, C> {

    /**
     * 被减数
     **/
    private final SingleValueElementDesc<F, C> minuend;

    /**
     * 减数
     **/
    private final SingleValueElementDesc<F, C> minus;

    public SubTract(SingleValueElementDesc<F, C> minuend, SingleValueElementDesc<F, C> minus) {
        this.minuend = minuend;
        this.minus = minus;
    }

    @Override
    public List<SingleValueElementDesc<?, ?>> getSingleValueList() {
        return Arrays.asList(minuend, minus);
    }

    @Override
    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
        return ((customCollector, customSingleValueWraps) -> {
            customCollector.append(customSingleValueWraps.get(0)).append("-").append(customSingleValueWraps.get(1));
        });
    }
}
