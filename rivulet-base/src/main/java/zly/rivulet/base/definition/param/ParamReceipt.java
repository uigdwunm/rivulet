package zly.rivulet.base.definition.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;

public interface ParamReceipt extends SingleValueElementDefinition {

    Class<?> getType();

    Convertor<?, ?> getConvertor();

    @Override
    default ParamReceipt forAnalyze() {
        return this;
    }
}
