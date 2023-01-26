package zly.rivulet.mysql.definer.outerType;

import zly.rivulet.base.utils.BooleanEnum;
import zly.rivulet.mysql.definer.outerType.feature.FloatingPoint;
import zly.rivulet.sql.exception.SQLModelDefineException;

public abstract class FixedPointNumericType implements FloatingPoint, NumericType {
    protected final int M;

    protected final int D;

    protected final boolean unSigned;

    private final boolean zerofill;

    public FixedPointNumericType(
            int M,
            int D,
            BooleanEnum unSigned,
            BooleanEnum zerofill
    ) {
        if (unSigned.isFalse() && zerofill.isTrue()) {
            throw SQLModelDefineException.zerofillMustUnSigned();
        }
        this.M = M;
        this.D = D;
        this.zerofill = zerofill.isTrue();
        this.unSigned = this.zerofill || unSigned.isTrue();
    }
}
