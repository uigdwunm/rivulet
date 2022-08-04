package zly.rivulet.base.preparser.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.runparser.param_manager.ParamManager;

/**
 * Description 对应每个查询方法都有一个
 *
 * @author zhaolaiyuan
 * Date 2022/2/26 9:25
 **/
public interface ParamDefinitionManager {

    ParamManager getParamManager(Object[] originParams);

    default ParamDefinition register(Param<?> paramDesc) {
        return this.register(paramDesc, null);
    }

    ParamDefinition register(Param<?> paramDesc, FieldMeta fieldMeta);

    Object getStaticParam(ParamDefinition paramDefinition);

    default String getStaticStatement(ParamDefinition paramDefinition) {
        Object param = this.getStaticParam(paramDefinition);
        Convertor<Object, ?> convertor = (Convertor<Object, ?>) paramDefinition.getConvertor();
        return convertor.convertToStatement(param);
    }
}
