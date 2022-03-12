package zly.rivulet.base.definition.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.describer.param.StaticParam;

public class StaticParamDefinition implements ParamDefinition {

    private final Object value;

    private Convertor<?, ?> convertor;

    public StaticParamDefinition(StaticParam<?> param, Convertor<?, ?> convertor) {
        this.value = param.getValue();
        this.convertor = convertor;
    }

    @Override
    public Object getParam(Object[] params) {
        return value;
    }

    @Override
    public Convertor<?, ?> getConverter() {
        return this.convertor;
    }
}
