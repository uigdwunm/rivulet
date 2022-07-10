package zly.rivulet.sql.preparser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.param.ParamDefinitionSQL;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.sql.definition.param.SqlParamDefinitionSQL;

import java.lang.reflect.Parameter;

public class SqlParamDefinitionManager extends ParamDefinitionManager {

    public SqlParamDefinitionManager(Parameter[] parameters, ConvertorManager convertorManager) {
        super(parameters, convertorManager);
    }

    @Override
    protected ParamDefinitionSQL createParamDefinition(Param<?> paramDesc, FieldMeta fieldMeta) {
        return new SqlParamDefinitionSQL(paramDesc, fieldMeta, super.convertorManager);
    }
}
