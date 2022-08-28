package zly.rivulet.base.generator.param_manager;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.exception.ParseException;

public interface ParamManager {

    Object getParam(ParamReceipt paramReceipt);

    default String getStatement(ParamReceipt paramReceipt) {
        Object param = this.getParam(paramReceipt);
        Convertor<Object, ?> convertor = (Convertor<Object, ?>) paramReceipt.getConvertor();
        if (!convertor.checkJavaType(param)) {
            throw ParseException.errorParamType(paramReceipt, param);
        }
        return convertor.convertToStatement(param);
    }

}
