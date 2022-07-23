package zly.rivulet.base.preparser.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;

public class EmptyParamDefinition implements ParamDefinition {

    public static final EmptyParamDefinition INSTANCE = new EmptyParamDefinition();

    @Override
    public Class<?> getClazz() {
        return null;
    }

    @Override
    public Convertor<?, ?> getConvertor() {
        return null;
    }

    @Override
    public Param<?> getOriginDesc() {
        return null;
    }
}
