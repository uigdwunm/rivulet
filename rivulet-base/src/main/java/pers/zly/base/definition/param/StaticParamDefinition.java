package pers.zly.base.definition.param;

import pers.zly.base.convertor.Convertor;
import pers.zly.base.describer.param.StaticParam;

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
