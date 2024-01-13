package zly.rivulet.base.describer.param;

public class StaticParam<T> extends Param<T> {

    private final T value;

    protected StaticParam(T value, ParamCheckType paramCheckType) {
        super((Class<T>) value.getClass(), paramCheckType);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}