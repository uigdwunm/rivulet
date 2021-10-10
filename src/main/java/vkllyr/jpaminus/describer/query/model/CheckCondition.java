package vkllyr.jpaminus.describer.query.model;

import vkllyr.jpaminus.describer.query.desc.Param;

public abstract class CheckCondition<P> {

    // 需要的参数数量
    private final int needParamsCount;

    // 需要的参数
    private final Param<P>[] params;

    // 解析好的参数
    private P[] paramValues;

    // 永远返回正确的，给不需要检查条件的
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

    public abstract boolean check(P ... paramValues);

}
