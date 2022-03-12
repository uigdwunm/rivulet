package zly.rivulet.base.definition.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.describer.param.Param;

public interface ParamDefinition extends Definition {

    Class<?> getClazz();

    Convertor<?, ?> getConvertor();

    Param<?> getOriginDesc();
}
