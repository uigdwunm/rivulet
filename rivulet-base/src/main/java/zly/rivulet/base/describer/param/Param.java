package zly.rivulet.base.describer.param;

import zly.rivulet.base.describer.SingleValueElementDesc;

/**
 * Description 需要通过反射获取的参数，只需要名称和类型就足够了,
 * 如果是在对象内部的参数，可以通过"."的方式指明路径，
 * 例如：有一个User对象，入参名为uu，获取其名为name的字段：Param.of(String.class, "uu.name")
 *
 * @author zhaolaiyuan
 * Date 2021/9/20 11:53
 **/
public abstract class Param<C> implements SingleValueElementDesc<C, C> {

    private final Class<C> paramType;

    /**
     * 参数检查方式，可以为空，后面取默认值
     **/
    private final ParamCheckType paramCheckType;

    protected Param(Class<C> paramType, ParamCheckType paramCheckType) {
        this.paramType = paramType;
        this.paramCheckType = paramCheckType;
    }

    protected Param(Class<C> paramType) {
        this(paramType, null);
    }

    public Class<C> getParamType() {
        return paramType;
    }

    public ParamCheckType getParamCheckType() {
        return paramCheckType;
    }

    public static <C> Param<C> of(Class<C> clazz, String pathKey, ParamCheckType paramCheckType) {
        return new StandardParam<>(clazz, pathKey, paramCheckType);
    }

    public static StaticParam<?> staticOf(Object paramValue) {
        return new StaticParam<>(paramValue);
    }
}
