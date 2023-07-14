package zly.rivulet.base.describer;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definer.enums.RivuletFlag;

public interface WholeDesc extends Desc {

    Class<?> getReturnType();

    RivuletDesc getAnnotation();

    void setAnnotation(RivuletDesc anno);

    RivuletFlag getFlag();
}
