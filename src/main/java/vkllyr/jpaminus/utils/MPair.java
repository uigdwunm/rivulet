package vkllyr.jpaminus.utils;

public class MPair<L, R> {

    private final L left;

    private final R right;

    private MPair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> MPair<L, R> of(L left, R right) {
        return new MPair<>(left, right);
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

}
