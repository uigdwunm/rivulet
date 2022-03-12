package zly.rivulet.base.definition.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.base.exception.ParamDefineException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.ArrayUtils;
import zly.rivulet.base.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

public class StandardParamDefinition implements ParamDefinition {

    // 定义时的参数路径（也可能直接参数值）
    private final String paramPath;

    // 上面的paramPath有可能是路径，这里paramName则一定是最终的参数名
    private final String paramName;

    // 是否以占位符的形式传参，默认是，否则会拼到语句中
    private final boolean isPlaceholder;

    // 参数类型
    private final Class<?> clazz;

    // 这个参数在源方法入参的索引位置
    private final int index;

    // 这个参数在源方法入参的索引路径
    private final Field[] fieldPathes;

    // 对应的转换器，从参数转换成语句的string
    private Convertor<?, ?> convertor;

    public StandardParamDefinition(StandardParam<?> param, Parameter[] parameters) {
        this(param, parameters, null);
    }

    public StandardParamDefinition(StandardParam<?> param, Parameter[] parameters, Convertor<?, ?> convertor) {
        String paramPath = param.getParamName();
        if (StringUtil.isBlank(paramPath)) {
            // 路径为空
            throw ParamDefineException.pathIllegal(param.getParamName());
        }
        String[] split = paramPath.split("\\.", 10);
        int pathLength = split.length;
        if (pathLength > 9) {
            // 路径过长
            throw ParamDefineException.pathTooLong(param.getParamName());
        }

        if (pathLength == 0) {
            // 解析后路径为空
            throw ParamDefineException.pathIllegal(param.getParamName());
        }
        this.paramPath = paramPath;
        this.isPlaceholder = param.isPlaceholder();
        this.clazz = param.getClazz();

        this.index = ArrayUtils.find(parameters, split[0], Parameter::getName);
        if (this.index == -1) {
            // 路径第一个值没找到
            throw ParamDefineException.pathIllegal(param.getParamName());
        }

        if (pathLength == 1) {
            this.paramName = paramPath;
        } else {
            this.paramName = ArrayUtils.getLast(parameters).getName();
        }

        this.fieldPathes = new Field[pathLength - 1];
        try {
            initParam(split, fieldPathes, 1, parameters[this.index].getType());
        } catch (NoSuchFieldException e) {
            // 属性名匹配不到
            throw ParamDefineException.pathIllegal(param.getParamName());
        }

        this.convertor = convertor;
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

    @Override
    public Object getParam(Object[] params) {
        Object firstParam = params[this.index];
        return this.getParam(this.fieldPathes, 0, firstParam);
    }

    @Override
    public Convertor<?, ?> getConverter() {
        return this.convertor;
    }

    /**
     * Description 是否sql原生占位符类型
     *
     * @author zhaolaiyuan
     * Date 2021/11/20 10:21
     **/
    public boolean isPlaceholder() {
        return isPlaceholder;
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
