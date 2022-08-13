package zly.rivulet.base.generator.param_manager;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.exception.ParseException;

public interface ParamManager {

    Object getParam(ParamDefinition paramDefinition);

    default String getStatement(ParamDefinition paramDefinition) {
        Object param = this.getParam(paramDefinition);
        Convertor<Object, ?> convertor = (Convertor<Object, ?>) paramDefinition.getConvertor();
        if (!convertor.checkJavaType(param)) {
            throw ParseException.errorParamType(paramDefinition, param);
        }
        return convertor.convertToStatement(param);
    }

}
