package zly.rivulet.base.generator.param_manager.for_proxy_method;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.generator.param_manager.ParamManager;

public interface ProxyMethodParamManager extends ParamManager {

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
