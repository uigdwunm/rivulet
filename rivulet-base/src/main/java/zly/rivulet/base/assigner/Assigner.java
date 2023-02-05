package zly.rivulet.base.assigner;

public interface Assigner<T> {

    /**
     * Description 取值
     *
     * @author zhaolaiyuan
     * Date 2022/10/13 8:15
     **/
    Object getValue(T results, int indexStart);

    int size();

}
