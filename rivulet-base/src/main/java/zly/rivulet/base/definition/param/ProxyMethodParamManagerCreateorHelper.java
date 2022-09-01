package zly.rivulet.base.definition.param;

import zly.rivulet.base.exception.ParamDefineException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.generator.param_manager.LazyParamManager;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.SimpleParamManager;
import zly.rivulet.base.utils.ArrayUtils;
import zly.rivulet.base.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ProxyMethodParamManagerCreateorHelper {

    public static Function<Object[], ParamManager> createParamManagerCreator(Method method, List<ParamReceipt> allParamReceiptList) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 1 && Map.class.isAssignableFrom(parameters[0].getType())) {
            // 参数只有一个并且是map类型
            return originParams -> new SimpleParamManager((Map<String, Object>) originParams[0]);
        } else {
            Map<String, Function<Object[], Object>> paramCreatorMap = new HashMap<>(allParamReceiptList.size());
            for (ParamReceipt paramReceipt : allParamReceiptList) {
                if (paramReceipt instanceof StaticParamReceipt) {
                    continue;
                } else if (paramReceipt instanceof PathKeyParamReceipt) {
                    PathKeyParamReceipt pathKeyParamReceipt = (PathKeyParamReceipt) paramReceipt;
                    String pathKey = pathKeyParamReceipt.getPathKey();
                    Function<Object[], Object> paramCreator = createParamCreator(pathKey, parameters);
                    paramCreatorMap.put(pathKey, paramCreator);
                } else {
                    throw UnbelievableException.unknownType();
                }
            }

            return originParams -> new LazyParamManager(originParams, paramCreatorMap);
        }
    }

    private static Function<Object[], Object> createParamCreator(String paramPath, Parameter[] parameters) {
        if (StringUtil.isBlank(paramPath)) {
            // 路径为空
            throw ParamDefineException.pathIllegal(paramPath);
        }
        String[] split = paramPath.split("\\.", 10);
        int pathLength = split.length;
        if (pathLength > 9) {
            // 路径过长
            throw ParamDefineException.pathTooLong(paramPath);
        }

        if (pathLength == 0) {
            // 解析后路径为空
            throw ParamDefineException.pathIllegal(paramPath);
        }

        int index = ArrayUtils.find(parameters, split[0], Parameter::getName);
        if (index == -1) {
            // 路径第一个值没找到
            throw ParamDefineException.pathIllegal(paramPath);
        }

        Field[] fieldPaths = new Field[pathLength - 1];

        try {
            parseParam(split, fieldPaths, 1, parameters[index].getType());
        } catch (NoSuchFieldException e) {
            // 属性名匹配不到
            throw ParamDefineException.pathIllegal(paramPath);
        }

        return params -> {
            Object firstParam = params[index];
            return getParam(fieldPaths, 0, firstParam);
        };
    }

    private static void parseParam(String[] paramName, Field[] fields, int i, Class<?> clazz) throws NoSuchFieldException {
        if (paramName.length == i) {
            return;
        }
        Field field = clazz.getDeclaredField(paramName[i]);
        fields[i - 1] = field;
        field.setAccessible(true);
        parseParam(paramName, fields, i + 1, field.getType());
    }

    private static Object getParam(Field[] fields, int i, Object obj) {
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
