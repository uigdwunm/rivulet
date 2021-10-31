package vkllyr.jpaminus.parser.definition.sqlPart;

import vkllyr.jpaminus.utils.ArrayUtils;
import vkllyr.jpaminus.describer.query.desc.Param;

public abstract class CheckCondition<P> implements InitParameter{

    // 需要的参数数量
    private final int needParamsCount;

    // 需要的参数
    private final Param<P>[] params;

    // 记录自己需要的参数在源方法入参中的索引位置
    private int[] paramIndexes;

    // 常量，给一个永远返回正确的，给不需要检查条件的
    public static final CheckCondition<?> IS_TRUE = new CheckCondition<Object>() {
        @Override
        public boolean check(Object... paramValues) {
            return true;
        }
    };

    public CheckCondition(Param<P> ... params) {
        this.params = params;
        this.needParamsCount = params.length;
    }

    protected abstract boolean check(Object ... paramValues);

    public boolean checkExist(Object ... paramValues) {
        Object param = ArrayUtils.fill(paramValues, paramIndexes);
        return this.check(param);
    }

    @Override
    public Param<?>[] getParams() {
        return params;
    }

    @Override
    public void setParamIndexes(int[] paramIndexes) {
        this.paramIndexes = paramIndexes;
    }
}
