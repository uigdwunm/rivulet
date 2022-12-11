package zly.rivulet.base.generator.param_manager.for_proxy_method;

import zly.rivulet.base.convertor.StatementConvertor;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.definition.param.PathKeyParamReceipt;
import zly.rivulet.base.definition.param.StaticParamReceipt;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.generator.param_manager.ParamManager;

public interface CommonParamManager extends ParamManager {

    Object getParam(String pathKey);

    default Object getParam(ParamReceipt paramReceipt) {
        if (paramReceipt instanceof StaticParamReceipt) {
            StaticParamReceipt staticParamReceipt = (StaticParamReceipt) paramReceipt;
            return staticParamReceipt.getParamValue();
        } else if (paramReceipt instanceof PathKeyParamReceipt) {
            PathKeyParamReceipt pathKeyParamReceipt = (PathKeyParamReceipt) paramReceipt;
            return this.getParam(pathKeyParamReceipt.getPathKey());
        } else {
            throw UnbelievableException.unknownType();
        }
    }

    default String getStatement(ParamReceipt paramReceipt) {
        Object param = this.getParam(paramReceipt);
        StatementConvertor<Object> convertor = paramReceipt.getConvertor();
        if (!convertor.checkJavaType(param)) {
            throw ParseException.errorParamType(paramReceipt, param);
        }
        return convertor.convert(param);
    }

}
