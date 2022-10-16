package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;

public class Divide<F, C> implements SQLFunction<F, C> {

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
}
