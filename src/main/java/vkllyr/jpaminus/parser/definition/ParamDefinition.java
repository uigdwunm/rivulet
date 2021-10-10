package vkllyr.jpaminus.parser.definition;

import vkllyr.jpaminus.describer.query.desc.Param;

public class ParamDefinition {

    private final Class<?> clazz;

    private final String paramName;

    // 这个参数在源方法入参的索引位置
    private final int index;

    public ParamDefinition(Param<?> param, int index) {
        this.clazz = param.getClazz();
        this.paramName = param.getParamName();
        this.index = index;
    }
}
