package zly.rivulet.base.utils;

/**
 * Description 一个单值容器
 *
 * @author zhaolaiyuan
 * Date 2022/9/18 10:04
 **/
public final class Scabbard<T> {

    private T sword;

    public void set(T sword) {
        // 值存在时，简单判断下
        if (this.isExists()) {
            if (this.sword != sword) {
                // 塞进来一个新的值，报错
                throw new IllegalArgumentException("每个Scabbard只允许保留一个值");
            } else {
                return;
            }
        }

        // 值不存在，双重判断下
        synchronized (this) {
            if (this.isExists()) {
                if (this.sword != sword) {
                    // 塞进来一个新的值，报错
                    throw new IllegalArgumentException("每个Scabbard只允许保留一个值");
                }
            } else {
                // 为空可以赋值
                this.sword = sword;
            }
        }
    }

    public T get() {
        return sword;
    }

    public boolean isNull() {
        return sword == null;
    }

    public boolean isExists() {
        return sword != null;
    }
}
