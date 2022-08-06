package zly.rivulet.sql.preparser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.sql.definition.param.SQLParamDefinition;

public class SqlParamDefinitionManager extends ParamDefinitionManager {

    public SqlParamDefinitionManager(ConvertorManager convertorManager) {
        super(convertorManager);
    }

    @Override
    protected ParamDefinition createParamDefinition(Param<?> paramDesc, FieldMeta fieldMeta) {
        return new SQLParamDefinition(paramDesc, fieldMeta, super.convertorManager);
    }
}
