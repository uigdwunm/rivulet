package pers.zly.base.definition.param;

import pers.zly.base.convertor.Convertor;
import pers.zly.base.convertor.ConvertorManager;
import pers.zly.base.convertor.StringStatement;
import pers.zly.base.definer.FieldMeta;
import pers.zly.base.describer.param.Param;
import pers.zly.base.describer.param.StandardParam;
import pers.zly.base.describer.param.StaticParam;
import pers.zly.base.exception.UnbelievableException;

import java.lang.reflect.Parameter;

public interface ParamDefinition {
    static ParamDefinition of(Param param, Parameter[] parameters) {
        if (param instanceof StandardParam) {
            return of((StandardParam<?>) param, parameters, null);
        } else if (param instanceof StaticParam) {
            return of((StaticParam<?>) param);
        }
        throw new UnbelievableException("未知的参数定义类型");
    }

    static ParamDefinition of(StandardParam<?> param, Parameter[] parameters, FieldMeta fieldMeta) {
        // TODO
        Convertor<?, StringStatement> convertor = ConvertorManager.get(
            param.getClazz(),
            null
        );
        return new StandardParamDefinition(param, parameters, convertor);
    }

    static ParamDefinition of(StaticParam<?> param) {
        Convertor<?, StringStatement> convertor = ConvertorManager.get(
            param.getValue().getClass(),
            StringStatement.class
        );
        return new StaticParamDefinition(param, convertor);
    }

    Object getParam(Object[] params);

    Convertor<?, ?> getConverter();
}
