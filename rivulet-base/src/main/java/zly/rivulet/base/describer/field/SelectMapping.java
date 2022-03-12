package zly.rivulet.base.describer.field;

/**
 * Description 代表返回值对象映射的字段，一定对应返回值对象中的set方法
 *
 * @author zhaolaiyuan
 * Date 2022/1/3 14:07
 **/
@FunctionalInterface
public interface SelectMapping<S, F> {

    void setMapping(S s, F f);

}
