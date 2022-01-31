package vkllyr.jpaminus.describer.query.definition.conditions;

import vkllyr.jpaminus.describer.Param;
import vkllyr.jpaminus.parser.definition.ParamDefinition;

import java.lang.reflect.Parameter;

public abstract class CheckCondition<P> {

    // 常量，给一个永远返回正确的，给不需要检查条件的
    public static final CheckCondition<?> IS_TRUE = new CheckCondition<Object>() {
        @Override
        public boolean check(Object... paramValues) {
            return true;
        }
    };


    // 传入的需要的参数
    private final Param[] params;

    // 解析后的参数
    private ParamDefinition[] paramDefinitions;

    public CheckCondition(Param... params) {
        this.params = params;
    }

    public void init(Parameter[] parameters) {
        int length = params.length;
        this.paramDefinitions = new ParamDefinition[length];
        for (int i = 0; i < length; i++) {
            paramDefinitions[i] = ParamDefinition.of(params[i], parameters);
        }
    }

    public boolean checkCondition(Object[] originParams) {
        int length = this.paramDefinitions.length;
        Object[] paramValues = new Object[length];
        for (int i = 0; i < length; i++) {
            paramValues[i] = paramDefinitions[i].getParam(originParams);
        }

        return check(paramValues);
    }

    protected abstract boolean check(Object ... paramValues);

}
