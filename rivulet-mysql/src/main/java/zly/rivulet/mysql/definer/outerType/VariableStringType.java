package zly.rivulet.mysql.definer.outerType;

import zly.rivulet.mysql.definer.outerType.feature.Variable;

public abstract class VariableStringType implements Variable, StringType {
    private final int length;

    protected VariableStringType(int length) {
        this.length = length;
    }
}
