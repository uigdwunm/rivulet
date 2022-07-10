package zly.rivulet.base.runparser.param_manager;

import zly.rivulet.base.definition.param.ParamDefinitionSQL;

public interface ParamManager {

    Object getParam(ParamDefinitionSQL paramDefinition);

    String getStatement(ParamDefinitionSQL paramDefinition);

}
