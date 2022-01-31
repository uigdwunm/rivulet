package pers.zly.base.describer.param;

public class StaticParam<T> implements Param<T> {

    private final T value;

    protected StaticParam(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}