package zly.rivulet.base.describer.field;

import zly.rivulet.base.describer.SingleValueElementDesc;

/**
 * Description 代表实体类具体字段的映射，一定对应一个get方法，代表的就是这个get方法对应的字段
 *
 * @author zhaolaiyuan
 * Date 2022/1/3 14:07
 **/
@FunctionalInterface
public interface JoinFieldMapping<C> extends SingleValueElementDesc<C, C> {

    C getMapping();

}
