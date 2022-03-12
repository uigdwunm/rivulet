package zly.rivulet.base.describer.param;


public class StandardParam<T> extends Param<T> {

    private final String pathKey;

    protected StandardParam(Class<T> clazz, String pathKey, ParamCheckType paramCheckType) {
        super(clazz, paramCheckType);
        this.pathKey = pathKey;
    }

    public String getPathKey() {
        return pathKey;
    }
}
