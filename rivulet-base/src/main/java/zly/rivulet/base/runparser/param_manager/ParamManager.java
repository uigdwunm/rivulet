package zly.rivulet.base.runparser.param_manager;

import zly.rivulet.base.definition.param.ParamDefinition;

public interface ParamManager {

    Object getParam(ParamDefinition paramDefinition);

    String getStatement(ParamDefinition paramDefinition);

}
