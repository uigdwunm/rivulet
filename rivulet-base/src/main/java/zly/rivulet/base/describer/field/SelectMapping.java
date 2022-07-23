package zly.rivulet.base.describer.field;

import java.io.Serializable;

/**
 * Description 代表返回值对象映射的字段，一定对应返回值对象中的set方法
 * @param <S> 容器对象
 * @param <F> 字段
 *
 * @author zhaolaiyuan
 * Date 2022/1/3 14:07
 **/
@FunctionalInterface
public interface SelectMapping<S, F> extends Serializable {

    void setMapping(S s, F f);

}
