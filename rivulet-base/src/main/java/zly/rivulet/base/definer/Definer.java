package zly.rivulet.base.definer;

/**
 * Description 将DO类解析成对象
 *
 * @author zhaolaiyuan
 * Date 2022/3/12 10:39
 **/
public interface Definer {

    ModelMeta parse(Class<?> clazz);
}
