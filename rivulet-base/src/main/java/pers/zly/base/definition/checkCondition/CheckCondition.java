package pers.zly.base.definition.checkCondition;


import pers.zly.base.definition.param.ParamDefinition;
import pers.zly.base.runparser.ParamManager;

public abstract class CheckCondition {

    // 常量，给一个永远返回正确的，给不需要检查条件的
    public static final CheckCondition IS_TRUE = new CheckCondition() {
        @Override
        public boolean check(Object... paramValues) {
            return true;
        }
    };

    // 解析后的参数
    private ParamDefinition[] paramDefinitions;

    public boolean checkCondition(ParamManager paramManager) {
        int length = this.paramDefinitions.length;
        Object[] paramValues = new Object[length];
        for (int i = 0; i < length; i++) {
            paramValues[i] = paramManager.getParam(paramDefinitions[i]);
        }

        return check(paramValues);
    }

    protected abstract boolean check(Object ... paramValues);

}
