package zly.rivulet.base.generator.param_manager.for_model_meta;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.generator.param_manager.ParamManager;

public interface ModelMetaParamManager extends ParamManager {

    Object getParam(Object model, ParamReceipt paramReceipt);

    default String getStatement(Object model, ParamReceipt paramReceipt) {
        Object param = this.getParam(model, paramReceipt);
        Convertor<Object, ?> convertor = (Convertor<Object, ?>) paramReceipt.getConvertor();
        if (!convertor.checkJavaType(param)) {
            throw ParseException.errorParamType(paramReceipt, param);
        }
        return convertor.convertToStatement(param);
    }
}