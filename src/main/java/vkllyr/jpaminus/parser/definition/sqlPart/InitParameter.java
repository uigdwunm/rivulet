package vkllyr.jpaminus.parser.definition.sqlPart;

import vkllyr.jpaminus.utils.ArrayUtils;
import vkllyr.jpaminus.describer.query.desc.Param;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public interface InitParameter {

    Param<?>[] getParams();

    void setParamIndexes(int[] result);

    /**
     * Description 传入方法签名中的参数列表（名称map对应好），解析每部分各自需要参数的索引位置
     *
     * @author zhaolaiyuan
     * Date 2021/10/16 9:45
     **/
    default void init(Map<String, Integer> parameterName2IndexMap) {
        Param<?>[] params = this.getParams();
        if (ArrayUtils.isEmpty(params)) {
            return;
        }
        int length = params.length;
        int[] result = new int[length];
        for (int i = 0; i <length; i++) {
            Param<?> param = params[i];
            result[i] = parameterName2IndexMap.get(param.getParamName());
        }

        this.setParamIndexes(result);
    }

    static Map<String, Integer> convertName2Index(Parameter[] parameters) {
        int length = parameters.length;
        Map<String, Integer> result = new HashMap<>(length);
        for (int i = 0; i < length; i++) {
            result.put(parameters[i].getName(), i);
        }

        return result;
    }
}
