package zly.rivulet.base.describer;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definer.enums.RivuletFlag;

public interface WholeDesc extends Desc {

    Class<?> getMainFrom();

    RivuletDesc getAnnotation();

    void setAnnotation(RivuletDesc anno);

    RivuletFlag getFlag();

    Class<?> getReturnType();
}
