package pers.zly.base.describer.param;


public class StandardParam<T> implements Param<T> {
    private final Class<T> clazz;

    private final String paramName;

    // 是否以占位符的形式传参，默认是，否则会拼到语句中
    private final boolean isPlaceholder;

    protected StandardParam(Class<T> clazz, String paramName, boolean isPlaceholder) {
        this.clazz = clazz;
        this.paramName = paramName;
        this.isPlaceholder = isPlaceholder;
    }

    public boolean isPlaceholder() {
        return isPlaceholder;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getParamName() {
        return paramName;
    }
}
