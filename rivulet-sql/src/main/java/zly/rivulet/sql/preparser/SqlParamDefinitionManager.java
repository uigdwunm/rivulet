package zly.rivulet.sql.preparser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.preparser.param.MultiParamDefinitionManager;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.sql.definition.param.SQLParamDefinition;

import java.lang.reflect.Parameter;

public class SqlParamDefinitionManager extends MultiParamDefinitionManager {

    public SqlParamDefinitionManager(Parameter[] parameters, ConvertorManager convertorManager) {
        super(parameters, convertorManager);
    }

    @Override
    protected ParamDefinition createParamDefinition(Param<?> paramDesc, FieldMeta fieldMeta) {
        return new SQLParamDefinition(paramDesc, fieldMeta, super.convertorManager);
    }
}
