package zly.rivulet.base.definition.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.param.Param;

public interface ParamDefinitionSQL extends SingleValueElementDefinition {

    Class<?> getClazz();

    Convertor<?, ?> getConvertor();

    Param<?> getOriginDesc();

    @Override
    default ParamDefinitionSQL forAnalyze() {
        return this;
    }
}
