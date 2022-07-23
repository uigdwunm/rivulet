package zly.rivulet.base.preparser.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.utils.StringUtil;

public class EmptyParamDefinitionManager extends ParamDefinitionManager {

    public EmptyParamDefinitionManager() {
        super(null, null);
    }

    @Override
    protected ParamDefinition createParamDefinition(Param<?> paramDesc, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public ParamManager getParamManager(Object[] originParams) {
        return new ParamManager() {
            @Override
            public Object getParam(ParamDefinition paramDefinition) {
                return StringUtil.Empty;
            }

            @Override
            public String getStatement(ParamDefinition paramDefinition) {
                return StringUtil.Empty;
            }
        };
    }

    @Override
    public ParamDefinition register(Param<?> paramDesc) {
        return EmptyParamDefinition.INSTANCE;
    }

    @Override
    public Object getStaticParam(ParamDefinition paramDefinition) {
        return StringUtil.Empty;
    }

    @Override
    public String getStaticStatement(ParamDefinition paramDefinition) {
        return StringUtil.Empty;
    }
}
