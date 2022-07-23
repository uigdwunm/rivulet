package zly.rivulet.base.describer;

public interface WholeDesc extends Desc {

    Class<?> getMainFrom();

    String getKey();

    void setKey(String key);
}
