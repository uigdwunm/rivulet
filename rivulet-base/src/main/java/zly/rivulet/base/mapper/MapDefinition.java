package zly.rivulet.base.mapper;

import zly.rivulet.base.describer.field.SelectMapping;

import java.util.List;

/**
 * Description 得到的返回值按顺序调用映射的mappingList
 *
 * @author zhaolaiyuan
 * Date 2022/6/5 20:42
 **/
public abstract class MapDefinition {

    public Class<?> getResultModelClass() {
        return null;
    }
}
