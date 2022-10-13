package zly.rivulet.base.assigner;

/**
 * Description 取值器
 *
 * @author zhaolaiyuan
 * Date 2022/10/13 8:20
 **/
public interface Picker<T> {

    Object getValue(T result, int startIndex);
}
