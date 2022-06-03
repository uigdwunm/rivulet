package zly.rivulet.mysql.definer.outerType;

import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.sql.exception.SQLModelDefineException;

public abstract class ExactNumericType implements Exact, NumericType {

    private final int maximumDisplayWidth;

    protected final boolean unSigned;

    private final boolean zerofill;

    public ExactNumericType(
        int maximumDisplayWidth,
        BooleanEnum unSigned,
        BooleanEnum zerofill
    ) {
        this.maximumDisplayWidth = maximumDisplayWidth;
        if (unSigned.isFalse() && zerofill.isTrue()) {
            throw SQLModelDefineException.zerofillMustUnSigned();
        }

        this.zerofill = zerofill.isTrue();
        this.unSigned = this.zerofill || unSigned.isTrue();
    }
}
