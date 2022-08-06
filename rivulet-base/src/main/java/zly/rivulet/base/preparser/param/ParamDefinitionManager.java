package zly.rivulet.base.preparser.param;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.base.describer.param.StaticParam;
import zly.rivulet.base.exception.ParamDefineException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.runparser.param_manager.LazyParamManager;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.runparser.param_manager.SimpleParamManager;
import zly.rivulet.base.utils.ArrayUtils;
import zly.rivulet.base.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Description 对应每个查询方法都有一个
 *
 * @author zhaolaiyuan
 * Date 2022/2/26 9:25
 **/
public abstract class ParamDefinitionManager {

    public final ConvertorManager convertorManager;

    public final List<ParamDefinition> allParamDefinitionList = new ArrayList<>();

    public final Map<Method, Function<Object[], ParamManager>> method_paramCreator_map = new ConcurrentHashMap<>();

    protected ParamDefinitionManager(ConvertorManager convertorManager) {
        this.convertorManager = convertorManager;
    }

    public ParamManager getParamManager(Method method, Object[] originParams) {
        Function<Object[], ParamManager> function = this.method_paramCreator_map.get(method);
        if (function == null) {
            function = this.registerMethod(method);
        }
        return function.apply(originParams);
    }

    public Function<Object[], ParamManager> registerMethod(Method method) {
        Function<Object[], ParamManager> paramManagerCreator = this.createParamManagerCreator(method);
        this.method_paramCreator_map.put(method, paramManagerCreator);
        return paramManagerCreator;
    }

    public Function<Object[], ParamManager> createParamManagerCreator(Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 1 && Map.class.isAssignableFrom(parameters[0].getType())) {
            // 参数只有一个并且是map类型
            return originParams -> new SimpleParamManager((Map<String, Object>) originParams[0]);
        } else {
            Map<ParamDefinition, Function<Object[], Object>> paramCreatorMap = new HashMap<>(this.allParamDefinitionList.size());
            for (ParamDefinition paramDefinition : allParamDefinitionList) {
                Param<?> paramDesc = paramDefinition.getOriginDesc();

                if (paramDesc instanceof StaticParam) {
                    continue;
                }
                Function<Object[], Object> paramCreator = this.createParamCreator((StandardParam<?>) paramDesc, parameters);
                paramCreatorMap.put(paramDefinition, paramCreator);
            }

            return originParams -> new LazyParamManager(originParams, paramCreatorMap);
        }
    }

    public ParamDefinition registerParam(Param<?> paramDesc) {
        return this.registerParam(paramDesc, null);
    }

    public ParamDefinition registerParam(Param<?> paramDesc, FieldMeta fieldMeta) {
        ParamDefinition paramDefinition = this.createParamDefinition(paramDesc, fieldMeta);

        this.allParamDefinitionList.add(paramDefinition);
        return paramDefinition;
    }

    protected abstract ParamDefinition createParamDefinition(Param<?> paramDesc, FieldMeta fieldMeta);

    private Function<Object[], Object> createParamCreator(StandardParam<?> standardParam, Parameter[] parameters) {
        String paramPath = standardParam.getPathKey();
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
            initParam(split, fieldPaths, 1, parameters[index].getType());
        } catch (NoSuchFieldException e) {
            // 属性名匹配不到
            throw ParamDefineException.pathIllegal(paramPath);
        }

        return params -> {
                Object firstParam = params[index];
                return this.getParam(fieldPaths, 0, firstParam);
            };
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

    private void initParam(String[] paramName, Field[] fields, int i, Class<?> clazz) throws NoSuchFieldException {
        if (paramName.length == i) {
            return;
        }
        Field field = clazz.getDeclaredField(paramName[i]);
        fields[i - 1] = field;
        field.setAccessible(true);
        initParam(paramName, fields, i + 1, field.getType());
    }

}
