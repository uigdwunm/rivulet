package zly.rivulet.base.definition.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.param.Param;

public interface ParamReceipt extends SingleValueElementDefinition {

    Class<?> getClazz();

    Convertor<?, ?> getConvertor();

    @Override
    default ParamReceipt forAnalyze() {
        return this;
    }
}
