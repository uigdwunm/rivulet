package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class Divide<F, C> implements Function<F, C> {

    /**
     * 被除数
     **/
    private final SingleValueElementDesc<F, C> divided;

    /**
     * 除数
     **/
    private final SingleValueElementDesc<F, C> divide;

    public Divide(SingleValueElementDesc<F, C> divided, SingleValueElementDesc<F, C> divide) {
        this.divided = divided;
        this.divide = divide;
    }

    public SingleValueElementDesc<F, C> getDivide() {
        return divide;
    }

    public SingleValueElementDesc<F, C> getDivided() {
        return divided;
    }

    @Override
    public List<SingleValueElementDesc<?, ?>> getSingleValueList() {
        return Arrays.asList(divided, divide);
    }

    @Override
    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
        return ((customCollector, customSingleValueWraps) -> {
            customCollector.append(customSingleValueWraps.get(0)).append("/").append(customSingleValueWraps.get(1));
        });
    }
}
