package zly.rivulet.base.utils;

/**
 * Description 有些方法需要返回两个结果时
 *
 * @author zhaolaiyuan
 * Date 2022/10/15 10:28
 **/
public final class PairReturn<L, R> {

    private final L left;

    private final R right;

    private PairReturn(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> PairReturn<L, R> of(L left, R right) {
        return new PairReturn<>(left, right);
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }
}
