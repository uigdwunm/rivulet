package pers.zly.base.utils;

import pers.zly.base.exception.UnbelievableException;

import java.lang.reflect.Field;

/**
 * Description 参数解析工具类
 *
 * @author zhaolaiyuan
 * Date 2021/12/18 11:38
 **/
public class ParamParseUtils {

    private void initParam(String[] paramName, Field[] fields, int i, Class<?> clazz) throws NoSuchFieldException {
        if (paramName.length == i) {
            return;
        }
        Field field = clazz.getDeclaredField(paramName[i]);
        fields[i - 1] = field;
        field.setAccessible(true);
        initParam(paramName, fields, i + 1, field.getType());
    }

    private Object getParam(Field[] fields, int i, Object obj) {
        if (fields.length == i) {
            return obj;
        }
        try {
            obj = fields[i].get(obj);
        } catch (IllegalAccessException e) {
            throw UnbelievableException.illegalAccess(e);
        }
        return getParam(fields, i + 1, obj);
    }
}
