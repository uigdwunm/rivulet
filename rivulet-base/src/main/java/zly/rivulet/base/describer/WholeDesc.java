package zly.rivulet.base.describer;

import zly.rivulet.base.definer.enums.RivuletFlag;

public interface WholeDesc extends Desc {

    Class<?> getMainFrom();

    String getKey();

    void setKey(String key);

    RivuletFlag getFlag();
}
