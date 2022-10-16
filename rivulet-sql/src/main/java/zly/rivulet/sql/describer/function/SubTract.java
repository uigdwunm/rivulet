package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;

public class SubTract<F, C> implements SQLFunction<F, C> {

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

    public SingleValueElementDesc<F, C> getMinuend() {
        return minuend;
    }

    public SingleValueElementDesc<F, C> getMinus() {
        return minus;
    }
}
