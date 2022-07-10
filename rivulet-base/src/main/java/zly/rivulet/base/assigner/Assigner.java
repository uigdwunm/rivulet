package zly.rivulet.base.assigner;

public interface Assigner<T> {

    Object assign(T results);

    Object buildContainer();

}
