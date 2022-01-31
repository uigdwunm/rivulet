package pers.zly.base.describer.param;

import pers.zly.base.describer.SingleValueElementDesc;

/**
 * Description 需要通过反射获取的参数，只需要名称和类型就足够了,
 * 如果是在对象内部的参数，可以通过"."的方式指明路径，
 * 例如：有一个User对象，入参名为uu，获取其名为name的字段：Param.of(String.class, "uu.name")
 *
 * @author zhaolaiyuan
 * Date 2021/9/20 11:53
 **/
public interface Param<C> extends SingleValueElementDesc<C, C> {

    /**
     * Description 普通参数
     *
     * @author zhaolaiyuan
     * Date 2021/11/27 11:25
     **/
    static <T> StandardParam<T> of(Class<T> clazz, String paramName) {
        return new StandardParam<>(clazz, paramName, true);
    }

    /**
     * Description 普通参数
     *
     * @author zhaolaiyuan
     * Date 2021/11/27 11:25
     **/
    static <T> StandardParam<T> of(Class<T> clazz, String paramName, boolean isPlaceholder) {
        return new StandardParam<>(clazz, paramName, isPlaceholder);
    }

    /**
     * Description 静态参数
     *
     * @author zhaolaiyuan
     * Date 2021/11/27 11:25
     **/
    static <T> StaticParam<T> staticOf(T value) {
        return new StaticParam<>(value);
    }


}
