package zly.rivulet.base.runparser.param_manager;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.param.ParamDefinition;

public interface ParamManager {

    Object getParam(ParamDefinition paramDefinition);

    default String getStatement(ParamDefinition paramDefinition) {
        Object param = this.getParam(paramDefinition);
        Convertor<Object, ?> convertor = (Convertor<Object, ?>) paramDefinition.getConvertor();
        return convertor.convertToStatement(param);
    }

}
