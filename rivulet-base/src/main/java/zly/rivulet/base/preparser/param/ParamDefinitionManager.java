package zly.rivulet.base.preparser.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.base.describer.param.StaticParam;
import zly.rivulet.base.exception.ParamDefineException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.runparser.param_manager.ProxyParamManager;
import zly.rivulet.base.utils.ArrayUtils;
import zly.rivulet.base.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
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

    private final Map<ParamDefinition, Function<Object[], Object>> paramCreatorMap = new ConcurrentHashMap<>();

    private final Map<ParamDefinition, Object> staticParamMap = new ConcurrentHashMap<>();

    private final Parameter[] parameters;

    protected final ConvertorManager convertorManager;

    public ParamDefinitionManager(Parameter[] parameters, ConvertorManager convertorManager) {
        this.parameters = parameters;
        this.convertorManager = convertorManager;
    }

    public ParamManager getParamManager(Object[] originParams) {
        // 静态的参数直接作为缓存传入
        Map<ParamDefinition, Object> staticParamMap = new HashMap<>(this.staticParamMap);
        return new ProxyParamManager(originParams, paramCreatorMap, staticParamMap);
    }

    protected abstract ParamDefinition createParamDefinition(Param<?> paramDesc, FieldMeta fieldMeta);

    public ParamDefinition register(Param<?> paramDesc) {
        return this.register(paramDesc, null);
    }

    private ParamDefinition register(Param<?> paramDesc, FieldMeta fieldMeta) {
        ParamDefinition paramDefinition = this.createParamDefinition(paramDesc, fieldMeta);

        if (paramDesc instanceof StaticParam) {
            StaticParam<?> staticParam = (StaticParam<?>) paramDesc;
            staticParamMap.put(paramDefinition, staticParam.getValue());
        } else {
            StandardParam<?> standardParam = (StandardParam<?>) paramDesc;

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

            paramCreatorMap.put(paramDefinition, params -> {
                Object firstParam = params[index];
                return this.getParam(fieldPaths, 0, firstParam);
            });
        }
        return paramDefinition;
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

    public Object getStaticParam(ParamDefinition paramDefinition) {
        return staticParamMap.get(paramDefinition);
    }

    public String getStaticStatement(ParamDefinition paramDefinition) {

        Object param = this.getStaticParam(paramDefinition);
        Convertor<Object, ?> convertor = (Convertor<Object, ?>) paramDefinition.getConvertor();
        return convertor.convertToStatement(param);
    }
}
