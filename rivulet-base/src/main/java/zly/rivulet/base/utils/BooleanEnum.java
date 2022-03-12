package zly.rivulet.base.utils;

public enum BooleanEnum {
    DEFAULT,
    TRUE,
    FALSE;

    public boolean isTrue() {
        return this.equals(TRUE);
    }

    public boolean isFalse() {
        return this.equals(FALSE);
    }
}
