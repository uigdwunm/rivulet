package zly.rivulet.base.definition.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.describer.param.Param;

public class StaticParamReceipt implements ParamReceipt {

    public StaticParamReceipt() {

    }

    @Override
    public Class<?> getClazz() {
        return null;
    }

    @Override
    public Convertor<?, ?> getConvertor() {
        return null;
    }
}
