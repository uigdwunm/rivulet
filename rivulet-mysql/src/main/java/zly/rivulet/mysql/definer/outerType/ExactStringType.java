package zly.rivulet.mysql.definer.outerType;

import zly.rivulet.mysql.definer.outerType.feature.Exact;

public abstract class ExactStringType implements Exact, StringType {

    private final int length;

    protected ExactStringType(int length) {
        this.length = length;
    }
}
