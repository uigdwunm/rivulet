package vkllyr.jpaminus.parser.definition;

import vkllyr.jpaminus.definer.field.FieldDefinition;
import vkllyr.jpaminus.describer.Param;
import vkllyr.jpaminus.exception.ParamDefineException;
import vkllyr.jpaminus.exception.UnbelievableException;
import vkllyr.jpaminus.utils.ArrayUtils;
import vkllyr.jpaminus.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * Description
 *
 * @author zhaolaiyuan
 * Date 2021/10/31 14:23
 **/
public interface ParamDefinition {

    static ParamDefinition of(Param param, Parameter[] parameters) {
        if (param instanceof Param.StandardParam) {
            return of((Param.StandardParam<?>) param, parameters, null);
        } else if (param instanceof Param.StaticParam) {
            return of((Param.StaticParam<?>) param);
        }
        throw new UnbelievableException("未知的参数定义类型");
    }

    static ParamDefinition of(Param.StandardParam<?> param, Parameter[] parameters, FieldDefinition fieldDefinition) {
        return new StandardParamDefinition(param, parameters, fieldDefinition);
    }

    static ParamDefinition of(Param.StaticParam<?> param) {
        return new StaticParamDefinition(param);
    }

    Object getParam(Object[] params);

    String getStatment(Object[] params);

    class StandardParamDefinition implements ParamDefinition {

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

        // 对应的字段定义，不一定有
        private final FieldDefinition fieldDefinition;

        public StandardParamDefinition(Param.StandardParam<?> param, Parameter[] parameters, FieldDefinition fieldDefinition) {
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

            this.fieldDefinition = fieldDefinition;
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
        public String getStatment(Object[] params) {
            Object param = getParam(params);

            // 字符类型的拼单引号
            // TODO
            return null;
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


    class StaticParamDefinition implements ParamDefinition {

        private final Class<?> clazz;

        private final Object value;

        public StaticParamDefinition(Param.StaticParam<?> param) {
            this.clazz = param.getClazz();
            this.value = param.getValue();
        }

        @Override
        public Object getParam(Object[] params) {
            return value;
        }

        @Override
        public String getStatment(Object[] params) {
            return (String) value;
        }
    }

}
